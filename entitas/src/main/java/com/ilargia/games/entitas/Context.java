package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.*;
import com.ilargia.games.entitas.api.events.*;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.group.Group;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Context<TEntity extends IEntity> implements IContext<TEntity> {

    // Eventos
    public Event<ContextEntityChanged> OnEntityCreated;
    public Event<ContextEntityChanged> OnEntityWillBeDestroyed;
    public Event<ContextEntityChanged> OnEntityDestroyed;
    public Event<ContextGroupChanged> OnGroupCreated;
    public Event<ContextGroupChanged> OnGroupCleared;

    public int _totalComponents;
    public Class<TEntity> entityType;
    protected Map<IMatcher, Group<TEntity>> _groups; //Object2ObjectArrayMap
    protected List<Group<TEntity>>[] _groupsForIndex; // ObjectArrayList
    private int _creationIndex;
    private Set<TEntity> _entities; //ObjectOpenHashSet
    private Stack<TEntity> _reusableEntities;
    private Set<TEntity> _retainedEntities; //ObjectOpenHashSet
    private TEntity[] _entitiesCache;
    private Map<String, IEntityIndex> _entityIndices; // Map
    private FactoryEntity<TEntity> _factoryEntiy;
    private ContextInfo _contextInfo;
    private Stack<IComponent>[] _componentContexts;
    EntityComponentChanged<TEntity> _cachedEntityChanged;


    public Context(int totalComponents, int startCreationIndex, ContextInfo metaData,
                  FactoryEntity<TEntity> factoryMethod) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _factoryEntiy = factoryMethod;

        OnEntityCreated = new Event<>();
        OnEntityWillBeDestroyed = new Event<>();
        OnEntityDestroyed = new Event<>();
        OnGroupCreated = new Event<>();
        OnGroupCleared = new Event<>();

        if (metaData != null) {
            _contextInfo = metaData;

            if (metaData.componentNames.length != totalComponents) {
                throw new ContextInfoException(this, metaData);
            }
        } else {
            String[] componentNames = new String[totalComponents];
            String prefix = "Index ";
            for (int i = 0; i < componentNames.length; i++) {
                componentNames[i] = prefix + i;
            }
            _contextInfo = new ContextInfo(
                    "Unnamed SplashPool", componentNames, null
            );
        }

        _groupsForIndex = new List[_totalComponents];
        _componentContexts = new Stack[totalComponents];
        _entityIndices = Collections.createMap(String.class, IEntityIndex.class);

        _reusableEntities = new Stack<>();
        _retainedEntities = Collections.createSet(Entity.class);
        _entities = Collections.createSet(Entity.class);
        _groups = Collections.createMap(IMatcher.class, Group.class);

        _cachedEntityChanged = (TEntity e, int idx, IComponent c) -> {
            updateGroupsComponentAddedOrRemoved(e, idx, c, _groupsForIndex);
        };
        entityType = (Class<TEntity>) _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo).getClass();

    }

    @Override
    public TEntity createEntity() {
        TEntity ent;
        if (_reusableEntities.size() > 0) {
            ent =  _reusableEntities.pop();
            ent.reactivate(_creationIndex++);
        } else {
            ent =  _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo);
            ent.initialize(_creationIndex++, _totalComponents, _componentContexts, _contextInfo);
        }

        _entities.add((TEntity) ent);
        ent.retain(this);
        _entitiesCache = null;
        Entity entity = (Entity) ent;
        entity.OnComponentAdded.addListener(_cachedEntityChanged);
        entity.OnComponentRemoved.addListener(_cachedEntityChanged);
        entity.OnComponentReplaced.addListener((EntityComponentReplaced<TEntity>) (TEntity e, int idx, IComponent pc, IComponent nc) -> {
            updateGroupsComponentReplaced(e, idx, pc, nc, _groupsForIndex);
        });
        entity.OnEntityReleased.addListener((EntityReleased<TEntity>) (TEntity e) -> {
            onEntityReleased(e, _retainedEntities, _reusableEntities);
        });
        notifyEntityCreated(this, ent);

        return ent;

    }

    @Override
    public void destroyEntity(TEntity entity) {
        if (!_entities.remove(entity)) {
            throw new ContextDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call pool.DestroyEntity() on a wrong pool?");
        }
        _entitiesCache = null;
        notifyEntityWillBeDestroyed(this, entity);

        entity.destroy();

        notifyEntityDestroyed(this, entity);

        if (entity.retainCount() == 1) {
            _reusableEntities.push(entity);
            entity.release(this);
            entity.removeAllOnEntityReleasedHandlers();

        } else {
            _retainedEntities.add(entity);
            entity.release(this);
        }

    }

    @Override
    public void destroyAllEntities() {
        for (TEntity entity : getEntities()) {
            destroyEntity(entity);
        }
        _entities.clear();

        if (_retainedEntities.size() != 0) {
            throw new ContextStillHasRetainedEntitiesException(this);
        }

    }

    @Override
    public boolean hasEntity(TEntity entity) {
        return _entities.contains(entity);
    }

    @Override
    public TEntity[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (TEntity[]) Array.newInstance(entityType, _entities.size());
            _entities.toArray(_entitiesCache);
        }
        return _entitiesCache;

    }

    @Override
    public int getTotalComponents() {
        return _totalComponents;
    }

    @Override
    public Group<TEntity> getGroup(IMatcher matcher) {
        Group<TEntity> group = null;
        if (!(_groups.containsKey(matcher) ? (group = _groups.get(matcher)) == group : false)) {

            group = new Group(matcher, entityType);
            for (TEntity entity : getEntities()) {
                group.handleEntitySilently(entity);
            }
            _groups.put(matcher, group);

            for (int index : matcher.getIndices()) {
                if (_groupsForIndex[index] == null) {
                    _groupsForIndex[index] = Collections.createList(Group.class);
                }
                _groupsForIndex[index].add(group);
            }
            notifyGroupCreated(this, group);

        }
        return group;

    }

    @Override
    public void clearGroups() {
        for (Group<TEntity> group : _groups.values()) {
            group.removeAllEventHandlers();
            for (IEntity entity : group.getEntities()) {
                entity.release(group);
            }
            notifyGroupCleared(this, group);
        }
        _groups.clear();

        for (int i = 0; i < _groupsForIndex.length; i++) {
            _groupsForIndex[i] = null;
        }
    }

    @Override
    public void addEntityIndex(String name, IEntityIndex entityIndex) {
        if (_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesAlreadyExistException(this, name);
        }
        _entityIndices.put(name, entityIndex);

    }

    @Override
    public IEntityIndex getEntityIndex(String name) {
        IEntityIndex entityIndex;
        if (!_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesNotExistException(this, name);
        } else {
            entityIndex = _entityIndices.get(name);
        }
        return entityIndex;

    }

    @Override
    public void deactivateAndRemoveEntityIndices() {
        for (IEntityIndex entityIndex : _entityIndices.values()) {
            entityIndex.deactivate();
        }
        _entityIndices.clear();
    }

    @Override
    public void resetCreationIndex() {
        _creationIndex = 0;
    }

    @Override
    public void clearComponentPool(int index) {
        Stack<IComponent> componentPool = _componentContexts[index];
        if (componentPool != null) {
            componentPool.clear();
        }
    }

    @Override
    public void clearComponentPools() {
        for (int i = 0; i < _componentContexts.length; i++) {
            clearComponentPool(i);
        }
    }

    @Override
    public void reset() {
        clearGroups();
        destroyAllEntities();
        resetCreationIndex();
        clearEventsPool();

    }

    public void updateGroupsComponentAddedOrRemoved(TEntity entity, int index, IComponent component, List<Group<TEntity>>[] groupsForIndex) {
        List<Group<TEntity>> groups = groupsForIndex[index];
        if (groups != null) {
            List<Event<GroupChanged<TEntity>>> events = EntitasCache.<TEntity>getGroupChangedList();

            for(int i = 0; i < groups.size(); i++) {
                events.add(groups.get(i).handleEntity(entity));
            }

            for (int i = 0, groupsCount = groups.size(); i < groupsCount; i++) {
                groups.get(i).handleEntity(entity, index, component);
            }
        }

    }

    protected void updateGroupsComponentReplaced(TEntity entity, int index, IComponent previousComponent,
                                                 IComponent newComponent, List<Group<TEntity>>[] groupsForIndex) {
        List<Group<TEntity>> groups = groupsForIndex[index];
        if (groups != null) {
            for (Group g : groups) {
                g.updateEntity(entity, index, previousComponent, newComponent);
            }
        }

    }

    protected void onEntityReleased(TEntity entity, Set<TEntity> retainedEntities, Stack<TEntity> reusableEntities) {
        if (entity.isEnabled()) {
            throw new EntityIsNotDestroyedException("Cannot release entity.");
        }
        retainedEntities.remove(entity);
        reusableEntities.push(entity);
    }

    @Override
    public Stack<IComponent>[] getComponentPools() {
        return _componentContexts;
    }

    @Override
    public ContextInfo getContextInfo() {
        return _contextInfo;
    }

    @Override
    public int getCount() {
        return _entities.size();
    }

    @Override
    public int getReusableEntitiesCount() {
        return _reusableEntities.size();
    }

    @Override
    public int getRetainedEntitiesCount() {
        return _retainedEntities.size();
    }

    public IEntity[] getEntities(IMatcher matcher) {
        return getGroup(matcher).getEntities();

    }

    @Override
    public void notifyEntityCreated(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityCreated.listeners()) {
            listener.changed(context, entity);
        }
    }

    @Override
    public void notifyEntityWillBeDestroyed(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityWillBeDestroyed.listeners()) {
            listener.changed(context, entity);
        }
    }

    @Override
    public void notifyEntityDestroyed(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityDestroyed.listeners()) {
            listener.changed(context, entity);
        }
    }

    @Override
    public void notifyGroupCreated(IContext context, IGroup group) {
        for (ContextGroupChanged listener : OnGroupCreated.listeners()) {
            listener.changed(context, group);
        }
    }

    @Override
    public void notifyGroupCleared(IContext context, IGroup group) {
        for (ContextGroupChanged listener : OnGroupCleared.listeners()) {
            listener.changed(context, group);
        }
    }

    @Override
    public void clearEventsPool() {
        OnEntityCreated.clear();
        OnEntityWillBeDestroyed.clear();
        OnEntityDestroyed.clear();
        OnGroupCreated.clear();
        OnGroupCleared.clear();
    }

    @Override
    public Collector createCollector(IMatcher matcher) {
        return new Collector(getGroup(matcher), GroupEvent.Added);
    }

    @Override
    public Collector createCollector(IMatcher matcher, GroupEvent groupEvent) {
        return new Collector(getGroup(matcher), groupEvent);
    }

    @Override
    public Collector createEntityCollector(Context[] contexts, IMatcher matcher) {
        return createEntityCollector(contexts, matcher, GroupEvent.Added);
    }

    @Override
    public Collector createEntityCollector(Context[] contexts, IMatcher matcher, GroupEvent eventType) {
        Group[] groups = new Group[contexts.length];
        GroupEvent[] eventTypes = new GroupEvent[contexts.length];

        for (int i = 0; i < contexts.length; i++) {
            groups[i] = contexts[i].getGroup(matcher);
            eventTypes[i] = eventType;
        }

        return new Collector(groups, eventTypes);
    }

}
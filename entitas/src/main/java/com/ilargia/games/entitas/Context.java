package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.*;
import com.ilargia.games.entitas.api.events.EntityComponentChanged;
import com.ilargia.games.entitas.api.events.EntityComponentReplaced;
import com.ilargia.games.entitas.api.events.EntityReleased;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.group.Group;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Context<TEntity extends IEntity> implements IContext<TEntity> {

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
    private EventBus<TEntity> _eventBus;
    private Stack<IComponent>[] _componentPools;


    public Context(int totalComponents, int startCreationIndex, ContextInfo metaData,
                   EventBus<TEntity> eventBus, FactoryEntity<TEntity> factoryMethod) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _factoryEntiy = factoryMethod;
        _eventBus = eventBus;

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

        EntityComponentChanged<TEntity> _cachedEntityChanged = (TEntity e, int idx, IComponent c) -> {
            updateGroupsComponentAddedOrRemoved(e, idx, c, _groupsForIndex);
        };
        _eventBus.OnComponentAdded.addListener(_cachedEntityChanged);
        _eventBus.OnComponentRemoved.addListener(_cachedEntityChanged);
        _eventBus.OnComponentReplaced.addListener((EntityComponentReplaced<TEntity>) (TEntity e, int idx, IComponent pc, IComponent nc) -> {
            updateGroupsComponentReplaced(e, idx, pc, nc, _groupsForIndex);
        });
        _eventBus.OnEntityReleased.addListener((EntityReleased<TEntity>) (TEntity e) -> {
            onEntityReleased(e, _retainedEntities, _reusableEntities);
        });

        entityType = (Class<TEntity>) _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo).getClass();

    }

    public Collector createEntityCollector(Context[] contexts, IMatcher matcher) {
        return createEntityCollector(contexts, matcher, GroupEvent.Added);
    }

    public Collector createEntityCollector(Context[] contexts, IMatcher matcher, GroupEvent eventType) {
        Group[] groups = new Group[contexts.length];
        GroupEvent[] eventTypes = new GroupEvent[contexts.length];

        for (int i = 0; i < contexts.length; i++) {
            groups[i] = contexts[i].getGroup(matcher);
            eventTypes[i] = eventType;
        }

        return new Collector(groups, eventTypes, _eventBus);
    }

    public TEntity createEntity() {
        TEntity ent;
        if(_reusableEntities.size() > 0) {
            ent = _reusableEntities.pop();
            ent.reactivate(_creationIndex++);
        } else {
            ent = _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo);
            ent.initialize(_creationIndex++, _totalComponents, _componentPools, _contextInfo);
        }

        _entities.add(ent);
        ent.retain(this);
        _entitiesCache = null;
//        entity.OnComponentAdded += _cachedEntityChanged;
//        entity.OnComponentRemoved += _cachedEntityChanged;
//        entity.OnComponentReplaced += _cachedComponentReplaced;
//        entity.OnEntityReleased += _cachedEntityReleased;
        _eventBus.notifyEntityCreated( this, ent);

        return ent;

    }

    public void destroyEntity(TEntity entity) {
        if (!_entities.remove(entity)) {
            throw new ContextDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call pool.DestroyEntity() on a wrong pool?");
        }
        _entitiesCache = null;
        _eventBus.notifyEntityWillBeDestroyed(this, entity);

        entity.destroy();

        _eventBus.notifyEntityDestroyed( this, entity);

        if (entity.retainCount() == 1) {
            _reusableEntities.push(entity);
            entity.release(this);
            entity.removeAllOnEntityReleasedHandlers();

        } else {
            _retainedEntities.add(entity);
            entity.release(this);
        }

    }

    public void destroyAllEntities() {
        for (TEntity entity : getEntities()) {
            destroyEntity(entity);
        }
        _entities.clear();

        if (_retainedEntities.size() != 0) {
            throw new ContextStillHasRetainedEntitiesException(this);
        }

    }

    public boolean hasEntity(TEntity entity) {
        return _entities.contains(entity);
    }

    public TEntity[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (TEntity[]) new Entity[_entities.size()];
            _entities.toArray(_entitiesCache);
        }
        return _entitiesCache;

    }

    @Override
    public int getTotalComponents() {
        return _totalComponents;
    }

    public Group<TEntity> getGroup(IMatcher matcher) {
        Group<TEntity> group = null;
        if (!(_groups.containsKey(matcher) ? (group = _groups.get(matcher)) == group : false)) {

            group = new Group(matcher, entityType, _eventBus);
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
            _eventBus.notifyGroupCreated( this, group);

        }
        return group;

    }

    public void clearGroups() {
        for (Group<TEntity> group : _groups.values()) {
            group.removeAllEventHandlers();
            for (IEntity entity : group.getEntities()) {
                entity.release(group);
            }
            _eventBus.notifyGroupCleared( this, group);
        }
        _groups.clear();

        for (int i = 0; i < _groupsForIndex.length; i++) {
            _groupsForIndex[i] = null;
        }
    }

    public void addEntityIndex(String name, IEntityIndex entityIndex) {
        if (_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesAlreadyExistException(this, name);
        }
        _entityIndices.put(name, entityIndex);

    }

    public IEntityIndex getEntityIndex(String name) {
        IEntityIndex entityIndex;
        if (!_entityIndices.containsKey(name)) {
            throw new ContextEntityIndexDoesNotExistException(this, name);
        } else {
            entityIndex = _entityIndices.get(name);
        }
        return entityIndex;

    }

    public void deactivateAndRemoveEntityIndices() {
        for (IEntityIndex entityIndex : _entityIndices.values()) {
            entityIndex.deactivate();
        }
        _entityIndices.clear();
    }

    public void resetCreationIndex() {
        _creationIndex = 0;
    }

    public void clearComponentPool(int index) {
        Stack<IComponent> componentPool = _componentContexts[index];
        if (componentPool != null) {
            componentPool.clear();
        }
    }

    public void clearComponentPools() {
        for (int i = 0; i < _componentContexts.length; i++) {
            clearComponentPool(i);
        }
    }

    public void reset() {
        clearGroups();
        destroyAllEntities();
        resetCreationIndex();
        _eventBus.clearEventsPool();

    }

    public void updateGroupsComponentAddedOrRemoved(TEntity entity, int index, IComponent component, List<Group<TEntity>>[] groupsForIndex) {
        List<Group<TEntity>> groups = groupsForIndex[index];
        if (groups != null) {
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

    public Stack<IComponent>[] getComponentPools() {
        return _componentContexts;
    }

    @Override
    public ContextInfo getContextInfo() {
        return null;
    }

    public ContextInfo getMetaData() {
        return _contextInfo;
    }

    public int getCount() {
        return _entities.size();
    }

    public int getReusableEntitiesCount() {
        return _reusableEntities.size();
    }

    public int getRetainedEntitiesCount() {
        return _retainedEntities.size();
    }

    public IEntity[] getEntities(IMatcher matcher) {
        return getGroup(matcher).getEntities();

    }


}
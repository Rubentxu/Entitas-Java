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
import java.util.*;

public class Context<TEntity extends Entity> implements IContext<TEntity> {

    public int _totalComponents;
    public Class<TEntity> entityType;
    // Eventos
    public Set<ContextEntityChanged> OnEntityCreated = Collections.createSet(ContextEntityChanged.class);
    public Set<ContextEntityChanged> OnEntityWillBeDestroyed = Collections.createSet(ContextEntityChanged.class);
    public Set<ContextEntityChanged> OnEntityDestroyed = Collections.createSet(ContextEntityChanged.class);
    public Set<ContextGroupChanged> OnGroupCreated = Collections.createSet(ContextGroupChanged.class);
    public Set<ContextGroupChanged> OnGroupCleared = Collections.createSet(ContextGroupChanged.class);
    protected Map<IMatcher, Group<TEntity>> _groups; //Object2ObjectArrayMap
    protected List<Group<TEntity>>[] _groupsForIndex; // ObjectArrayList
    protected EntityComponentChanged<TEntity> _cachedEntityChanged;
    private UUID id = UUID.randomUUID();
    private int _creationIndex;
    private Set<TEntity> _entities; //ObjectOpenHashSet
    private Stack<TEntity> _reusableEntities;
    private Set<TEntity> _retainedEntities; //ObjectOpenHashSet
    private TEntity[] _entitiesCache;
    private Map<String, IEntityIndex> _entityIndices; // Map
    private FactoryEntity<TEntity> _factoryEntiy;
    private ContextInfo _contextInfo;
    private Stack<IComponent>[] _componentContexts;


    public Context(int totalComponents, int startCreationIndex, ContextInfo contexInfo,
                   FactoryEntity<TEntity> factoryMethod) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _factoryEntiy = factoryMethod;

        if (contexInfo != null) {
            _contextInfo = contexInfo;

            if (contexInfo.componentNames.length != totalComponents) {
                throw new ContextInfoException(this, contexInfo);
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
            ent = _reusableEntities.pop();
            ent.reactivate(_creationIndex++);
        } else {
            ent = _factoryEntiy.create(_totalComponents, _componentContexts, _contextInfo);
            ent.initialize(_creationIndex++, _totalComponents, _componentContexts, _contextInfo);
        }

        _entities.add((TEntity) ent);
        ent.retain(this);
        _entitiesCache = null;
        Entity entity = (Entity) ent;
        entity.OnComponentAdded(_cachedEntityChanged);
        entity.OnComponentRemoved(_cachedEntityChanged);
        entity.OnComponentReplaced((EntityComponentReplaced<TEntity>) (TEntity e, int idx, IComponent pc, IComponent nc) -> {
            updateGroupsComponentReplaced(e, idx, pc, nc, _groupsForIndex);
        });
        entity.OnEntityReleased((EntityReleased<TEntity>) (TEntity e) -> {
            onEntityReleased(e, _retainedEntities, _reusableEntities);
        });
        notifyEntityCreated(ent);

        return ent;

    }

    @Override
    public void destroyEntity(TEntity entity) {
        if (!_entities.remove(entity)) {
            throw new ContextDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call pool.DestroyEntity() on a wrong pool?");
        }
        _entitiesCache = null;
        notifyEntityWillBeDestroyed(entity);

        entity.destroy();

        notifyEntityDestroyed(entity);

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
        if (!(_groups.containsKey(matcher) ? (group = _groups.get(matcher)) == null : false)) {

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
            notifyGroupCreated(group);

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
            notifyGroupCleared(group);
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
        clearEventsListener();

    }

    public void updateGroupsComponentAddedOrRemoved(TEntity entity, int index, IComponent component, List<Group<TEntity>>[] groupsForIndex) {
        List<Group<TEntity>> groups = groupsForIndex[index];
        if (groups != null) {
            List<Set<GroupChanged>> events = EntitasCache.getGroupChangedList();

            for (int i = 0; i < groups.size(); i++) {
                events.add(groups.get(i).handleEntity(entity));
            }

            for (int i = 0; i < events.size(); i++) {
                Set<GroupChanged> groupChangedEvent = events.get(i);
                if (groupChangedEvent != null) {
                    for (GroupChanged listener : groupChangedEvent) {
                        listener.changed(groups.get(i), entity, index, component);
                    }
                    ;
                }
            }
            EntitasCache.pushGroupChangedList(events);
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
        entity.removeAllOnEntityReleasedHandlers();
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

    public void clearEventsListener() {
        if (OnEntityCreated != null) OnEntityCreated.clear();
        if (OnEntityWillBeDestroyed != null) OnEntityWillBeDestroyed.clear();
        if (OnEntityDestroyed != null) OnEntityDestroyed.clear();
        if (OnGroupCreated != null) OnGroupCreated.clear();
        if (OnGroupCleared != null) OnGroupCleared.clear();

    }

    public void OnEntityCreated(ContextEntityChanged listener) {
        if (OnEntityCreated != null) {
            OnEntityCreated = Collections.createSet(ContextEntityChanged.class);
        }
        OnEntityCreated.add(listener);
    }

    public void OnEntityWillBeDestroyed(ContextEntityChanged listener) {
        if (OnEntityWillBeDestroyed != null) {
            OnEntityWillBeDestroyed = Collections.createSet(ContextEntityChanged.class);
        }
        OnEntityWillBeDestroyed.add(listener);
    }

    public void OnEntityDestroyed(ContextEntityChanged listener) {
        if (OnEntityDestroyed != null) {
            OnEntityDestroyed = Collections.createSet(ContextEntityChanged.class);
        }
        OnEntityDestroyed.add(listener);
    }

    public void OnGroupCreated(ContextGroupChanged listener) {
        if (OnGroupCreated != null) {
            OnGroupCreated = Collections.createSet(ContextGroupChanged.class);
        }
        OnGroupCreated.add(listener);
    }

    public void OnGroupCleared(ContextGroupChanged listener) {
        if (OnGroupCleared != null) {
            OnGroupCleared = Collections.createSet(ContextGroupChanged.class);
        }
        OnGroupCleared.add(listener);
    }

    public void notifyEntityCreated(IEntity entity) {
        if (OnEntityCreated != null) {
            for (ContextEntityChanged listener : OnEntityCreated) {
                listener.changed(this, entity);
            }
        }
    }


    public void notifyEntityWillBeDestroyed(IEntity entity) {
        if (OnEntityWillBeDestroyed != null) {
            for (ContextEntityChanged listener : OnEntityWillBeDestroyed) {
                listener.changed(this, entity);
            }
        }
    }


    public void notifyEntityDestroyed(IEntity entity) {
        if (OnEntityDestroyed != null) {
            for (ContextEntityChanged listener : OnEntityDestroyed) {
                listener.changed(this, entity);
            }
        }
    }


    public void notifyGroupCreated(IGroup group) {
        if (OnGroupCreated != null) {
            for (ContextGroupChanged listener : OnGroupCreated) {
                listener.changed(this, group);
            }
        }
    }


    public void notifyGroupCleared(IGroup group) {
        if (OnGroupCleared != null) {
            for (ContextGroupChanged listener : OnGroupCleared) {
                listener.changed(this, group);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Context<?> context = (Context<?>) o;

        if (_totalComponents != context._totalComponents) return false;
        if (id != null ? !id.equals(context.id) : context.id != null) return false;
        if (entityType != null ? !entityType.equals(context.entityType) : context.entityType != null) return false;
        return _contextInfo != null ? _contextInfo.equals(context._contextInfo) : context._contextInfo == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + _totalComponents;
        result = 31 * result + (entityType != null ? entityType.hashCode() : 0);
        result = 31 * result + (_contextInfo != null ? _contextInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Context{" +
                "_totalComponents=" + _totalComponents +
                ", entityType=" + entityType +
                ", _groups=" + _groups +
                ", _groupsForIndex=" + Arrays.toString(_groupsForIndex) +
                ", _creationIndex=" + _creationIndex +
                ", _entities=" + _entities +
                ", _reusableEntities=" + _reusableEntities +
                ", _retainedEntities=" + _retainedEntities +
                ", _entitiesCache=" + Arrays.toString(_entitiesCache) +
                ", _entityIndices=" + _entityIndices +
                ", _factoryEntiy=" + _factoryEntiy +
                ", _contextInfo=" + _contextInfo +
                ", _componentContexts=" + Arrays.toString(_componentContexts) +
                ", _cachedEntityChanged=" + _cachedEntityChanged +
                '}';
    }
}
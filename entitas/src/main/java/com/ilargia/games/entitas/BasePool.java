package com.ilargia.games.entitas;

import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.FactoryEntity;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.IEntityIndex;
import com.ilargia.games.entitas.interfaces.IMatcher;
import com.ilargia.games.entitas.interfaces.events.ComponentReplaced;
import com.ilargia.games.entitas.interfaces.events.EntityChanged;
import com.ilargia.games.entitas.interfaces.events.EntityReleased;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Stack;

public class BasePool<E extends Entity, P extends BasePool> {

    public int _totalComponents;
    public Class<E> entityType;
    protected Object2ObjectArrayMap<IMatcher, Group<E>> _groups;
    protected ObjectArrayList<Group<E>>[] _groupsForIndex;
    private int _creationIndex;
    private ObjectOpenHashSet<E> _entities;
    private Stack<E> _reusableEntities;
    private ObjectOpenHashSet<E> _retainedEntities;
    private E[] _entitiesCache;
    private EntityMetaData _metaData;
    private Stack<IComponent>[] _componentPools;
    private Object2ObjectArrayMap<String, IEntityIndex> _entityIndices;
    private FactoryEntity<E> _factoryEntiy;
    private EventBus<E> _eventBus;


    public BasePool(int totalComponents, int startCreationIndex, EntityMetaData metaData,
                    EventBus<E> eventBus, FactoryEntity<E> factoryMethod) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _factoryEntiy = factoryMethod;
        _eventBus = eventBus;

        if (metaData != null) {
            _metaData = metaData;

            if (metaData.componentNames.length != totalComponents) {
                throw new PoolMetaDataException(this, metaData);
            }
        } else {
            String[] componentNames = new String[totalComponents];
            String prefix = "Index ";
            for (int i = 0; i < componentNames.length; i++) {
                componentNames[i] = prefix + i;
            }
            _metaData = new EntityMetaData(
                    "Unnamed SplashPool", componentNames, null
            );
        }

        _groupsForIndex = new ObjectArrayList[totalComponents];
        _componentPools = new Stack[totalComponents];
        _entityIndices = new Object2ObjectArrayMap<>();

        _reusableEntities = new Stack<>();
        _retainedEntities = new ObjectOpenHashSet<>();
        _entities = new ObjectOpenHashSet<>();
        _groups = new Object2ObjectArrayMap<>();

        EntityChanged<E> _cachedEntityChanged = (E e, int idx, IComponent c) -> {
            updateGroupsComponentAddedOrRemoved(e, idx, c, _groupsForIndex);
        };
        _eventBus.OnComponentAdded.addListener(_cachedEntityChanged);
        _eventBus.OnComponentRemoved.addListener(_cachedEntityChanged);
        _eventBus.OnComponentReplaced.addListener((ComponentReplaced<E>) (E e, int idx, IComponent pc, IComponent nc) -> {
            updateGroupsComponentReplaced(e, idx, pc, nc, _groupsForIndex);
        });
        _eventBus.OnEntityReleased.addListener((EntityReleased<E>) (E e) -> {
            onEntityReleased(e, _retainedEntities, _reusableEntities);
        });

        entityType = (Class<E>) _factoryEntiy.create(_totalComponents, _componentPools, _metaData).getClass();

    }

    public static EntityCollector createEntityCollector(BasePool[] pools, IMatcher matcher) {
        return createEntityCollector(pools, matcher, GroupEventType.OnEntityAdded);
    }

    public static EntityCollector createEntityCollector(BasePool[] pools, IMatcher matcher, GroupEventType eventType) {
        Group[] groups = new Group[pools.length];
        GroupEventType[] eventTypes = new GroupEventType[pools.length];

        for (int i = 0; i < pools.length; i++) {
            groups[i] = pools[i].getGroup(matcher);
            eventTypes[i] = eventType;
        }

        return new EntityCollector(groups, eventTypes);
    }

    public E createEntity() {
        E ent = _reusableEntities.size() > 0
                ? _reusableEntities.pop()
                : _factoryEntiy.create(_totalComponents, _componentPools, _metaData);
        ent.setEnabled(true);
        ent.setCreationIndex(_creationIndex++);
        ent.retain(this);
        _entities.add(ent);
        _entitiesCache = null;
        _eventBus.notifyEntityCreated((P) this, ent);

        return ent;

    }

    public void destroyEntity(E entity) {
        if (!_entities.remove(entity)) {
            throw new PoolDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call pool.DestroyEntity() on a wrong pool?");
        }
        _entitiesCache = null;
        _eventBus.notifyEntityWillBeDestroyed((P) this, entity);

        entity.destroy();

        _eventBus.notifyEntityDestroyed((P) this, entity);

        if (entity.getRetainCount() != 1) {
            _retainedEntities.add(entity);
        }
        entity.release(this);
    }

    public void destroyAllEntities() {
        for (E entity : getEntities()) {
            destroyEntity(entity);
        }
        _entities.clear();

        if (_retainedEntities.size() != 0) {
            throw new PoolStillHasRetainedEntitiesException(this);
        }

    }

    public boolean hasEntity(E entity) {
        return _entities.contains(entity);
    }

    public E[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (E[]) new Entity[_entities.size()];
            _entities.toArray(_entitiesCache);
        }
        return _entitiesCache;

    }

    public Group<E> getGroup(IMatcher matcher) {
        Group<E> group = null;
        if (!(_groups.containsKey(matcher) ? (group = _groups.get(matcher)) == group : false)) {

            group = new Group(matcher, entityType);
            for (E entity : getEntities()) {
                group.handleEntitySilently(entity);
            }
            _groups.put(matcher, group);

            for (int index : matcher.getIndices()) {
                if (_groupsForIndex[index] == null) {
                    _groupsForIndex[index] = new ObjectArrayList<Group<E>>();
                }
                _groupsForIndex[index].add(group);
            }
            _eventBus.notifyGroupCreated((P) this, group);

        }
        return group;

    }

    public void clearGroups() {
        for (Group<E> group : _groups.values()) {
            group.removeAllEventHandlers();
            for (Entity entity : group.getEntities()) {
                entity.release(group);
            }
            _eventBus.notifyGroupCleared((P) this, group);
        }
        _groups.clear();

        for (int i = 0; i < _groupsForIndex.length; i++) {
            _groupsForIndex[i] = null;
        }
    }

    public void addEntityIndex(String name, IEntityIndex entityIndex) {
        if (_entityIndices.containsKey(name)) {
            throw new PoolEntityIndexDoesAlreadyExistException(this, name);
        }
        _entityIndices.put(name, entityIndex);

    }

    public IEntityIndex getEntityIndex(String name) {
        IEntityIndex entityIndex;
        if (!_entityIndices.containsKey(name)) {
            throw new PoolEntityIndexDoesNotExistException(this, name);
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
        Stack<IComponent> componentPool = _componentPools[index];
        if (componentPool != null) {
            componentPool.clear();
        }
    }

    public void clearComponentPools() {
        for (int i = 0; i < _componentPools.length; i++) {
            clearComponentPool(i);
        }
    }

    public void reset() {
        clearGroups();
        destroyAllEntities();
        resetCreationIndex();
        _eventBus.clearEventsPool();

    }

    public void updateGroupsComponentAddedOrRemoved(E entity, int index, IComponent component, ObjectArrayList<Group<E>>[] groupsForIndex) {
        ObjectArrayList<Group<E>> groups = groupsForIndex[index];
        if (groups != null) {
            ObjectArrayList<GroupChanged> events = EntitasCache.getGroupChangedList();
            for (int i = 0, groupsCount = groups.size(); i < groupsCount; i++) {
                events.add(groups.get(i).handleEntity(entity));
            }
            for (int i = 0; i < events.size(); i++) {
                GroupChanged groupChangedEvent = events.get(i);
                if (groupChangedEvent != null) {
                    groupChangedEvent.groupChanged(groups.get(i), entity, index, component);
                }
            }
            EntitasCache.pushGroupChangedList(events);
        }

    }

    protected void updateGroupsComponentReplaced(E entity, int index, IComponent previousComponent,
                                                 IComponent newComponent, ObjectArrayList<Group<E>>[] groupsForIndex) {
        ObjectArrayList<Group<E>> groups = groupsForIndex[index];
        if (groups != null) {
            for (Group g : groups) {
                g.updateEntity(entity, index, previousComponent, newComponent);
            }
        }

    }

    protected void onEntityReleased(E entity, ObjectOpenHashSet<E> retainedEntities, Stack<E> reusableEntities) {
        if (entity.isEnabled()) {
            throw new EntityIsNotDestroyedException("Cannot release entity.");
        }
        retainedEntities.remove(entity);
        reusableEntities.push(entity);
    }

    public Stack<IComponent>[] getComponentPools() {
        return _componentPools;
    }

    public EntityMetaData getMetaData() {
        return _metaData;
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

    public Entity[] getEntities(IMatcher matcher) {
        return getGroup(matcher).getEntities();

    }


}
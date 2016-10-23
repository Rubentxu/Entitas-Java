package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.*;

import java.util.*;

public class Pool {

    public PoolChanged OnEntityCreated;
    public PoolChanged OnEntityWillBeDestroyed;
    public PoolChanged OnEntityDestroyed;
    public PoolGroupChanged OnGroupCreated;
    public PoolGroupChanged OnGroupCleared;
    public EntityChanged _cachedEntityChanged;
    public ComponentReplaced _cachedComponentReplaced;
    public EntityReleased _cachedEntityReleased;

    public int _totalComponents;
    private int _creationIndex;
    private ObjectSet<Entity> _entities;
    private Stack<Entity> _reusableEntities;
    private ObjectSet<Entity> _retainedEntities;
    private Entity[] _entitiesCache;
    private PoolMetaData _metaData;
    protected ObjectMap<IMatcher, Group> _groups;
    protected Array<Group>[] _groupsForIndex;
    private Stack<IComponent>[] _componentPools;
    private ObjectMap<String, IEntityIndex> _entityIndices;


    public Pool(int totalComponents) {
        this(totalComponents, 0, null);
    }

    public Pool(int totalComponents, int startCreationIndex, PoolMetaData metaData) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;

        if(metaData != null) {
            _metaData = metaData;

            if(metaData.componentNames.length != totalComponents) {
                throw new PoolMetaDataException(this, metaData);
            }
        } else {
            String[] componentNames = new String[totalComponents];
                String prefix = "Index ";
            for (int i = 0; i < componentNames.length; i++) {
                componentNames[i] = prefix + i;
            }
            _metaData = new PoolMetaData(
                    "Unnamed Pool", componentNames, null
            );
        }

        _groupsForIndex = new Array[totalComponents];
        _componentPools = new Stack[totalComponents];
        _entityIndices = new ObjectMap<>();

        _cachedEntityChanged = (Entity e, int idx, IComponent c)-> { updateGroupsComponentAddedOrRemoved(e, idx, c); };
        _cachedComponentReplaced = (Entity e, int idx, IComponent pc, IComponent nc)
                -> { updateGroupsComponentReplaced(e, idx, pc, nc); };
        _cachedEntityReleased = (Entity e)-> { onEntityReleased(e); };

        _reusableEntities = new Stack<>();
        _retainedEntities = new ObjectSet<>();
        _entities = new ObjectSet<>();
        _groups = new ObjectMap<>();

    }


    public Entity createEntity() {
        Entity ent = _reusableEntities.size() > 0
                ? _reusableEntities.pop()
                : new Entity(_totalComponents, _componentPools, _metaData);
        ent.setEnabled(true);
        ent.setCreationIndex(_creationIndex++);
        ent.retain(this);
        _entities.add(ent);
        _entitiesCache = null;

        ent.OnComponentAdded = _cachedEntityChanged;
        ent.OnComponentRemoved = _cachedEntityChanged;
        ent.OnComponentReplaced = _cachedComponentReplaced;
        ent.OnEntityReleased = _cachedEntityReleased;

        if (OnEntityCreated != null) {
            OnEntityCreated.poolChanged(this, ent);
        }
        return ent;

    }

    public void destroyEntity(Entity entity) {
        if (!_entities.remove(entity)) {
            throw new PoolDoesNotContainEntityException("'" + this + "' cannot destroy " + entity + "!",
                    "Did you call pool.DestroyEntity() on a wrong pool?");
        }
        _entitiesCache = null;

        if (OnEntityWillBeDestroyed != null) {
            OnEntityWillBeDestroyed.poolChanged(this, entity);
        }

        entity.destroy();

        if (OnEntityDestroyed != null) {
            OnEntityDestroyed.poolChanged(this, entity);
        }

        if (entity.getRetainCount() == 1) {
            entity.OnEntityReleased = null;
            _reusableEntities.push(entity);
            entity.release(this);
            entity.removeAllOnEntityReleasedHandlers();
        } else {
            _retainedEntities.add(entity);
            entity.release(this);
        }

    }

    public void destroyAllEntities() {
        for (Entity entity : getEntities()) {
            destroyEntity(entity);
        }
        _entities.clear();

        if (_retainedEntities.size != 0) {
            throw new PoolStillHasRetainedEntitiesException(this);
        }

    }

    public boolean hasEntity(Entity entity) {
        return _entities.contains(entity);
    }

    public Entity[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = new Entity[_entities.size];
            int i = 0;
            for (Entity e: _entities) {
                _entitiesCache[i] = e;
                i++;
            }

        }
        return _entitiesCache;

    }

    public Group getGroup(IMatcher matcher) {
        Group group = null;
        if (!(_groups.containsKey(matcher) ? (group = _groups.get(matcher)) == group : false)) {
            group = new Group(matcher);
            for (Entity entity : getEntities()) {
                group.handleEntitySilently(entity);
            }
            _groups.put(matcher, group);

            for (int index : matcher.getindices()) {
                if (_groupsForIndex[index] == null) {
                    _groupsForIndex[index] = new Array<Group>();
                }
                _groupsForIndex[index].add(group);
            }

            if (OnGroupCreated != null) {
                OnGroupCreated.groupChanged(this, group);
            }
        }
        return group;

    }

    public void clearGroups() {
        for (Group group : _groups.values()) {
            group.removeAllEventHandlers();
            for (Entity entity : group.getEntities()) {
                entity.release(group);
            }

            if (OnGroupCleared != null) {
                OnGroupCleared.groupChanged(this, group);
            }
        }
        _groups.clear();

        for (int i = 0; i < _groupsForIndex.length; i++) {
            _groupsForIndex[i] = null;
        }
    }

    public void addEntityIndex(String name, IEntityIndex entityIndex) {
        if(_entityIndices.containsKey(name)) {
            throw new PoolEntityIndexDoesAlreadyExistException(this, name);
        }
        _entityIndices.put(name, entityIndex);

    }

    public IEntityIndex getEntityIndex(String name) {
        IEntityIndex entityIndex;
        if(!_entityIndices.containsKey(name)) {
            throw new PoolEntityIndexDoesNotExistException(this, name);
        } else {
            entityIndex = _entityIndices.get(name);
        }
        return entityIndex;

    }

    public void DeactivateAndRemoveEntityIndices() {
        for( IEntityIndex entityIndex : _entityIndices.values()) {
            entityIndex.deactivate();
        }
        _entityIndices.clear();
    }


    public void resetCreationIndex() {
        _creationIndex = 0;
    }

    public void ClearComponentPool(int index) {
        Stack<IComponent> componentPool = _componentPools[index];
        if(componentPool != null) {
            componentPool.clear();
        }
    }


    public void ClearComponentPools() {
        for (int i = 0; i < _componentPools.length; i++) {
            ClearComponentPool(i);
        }
    }

    public void reset() {
        clearGroups();
        destroyAllEntities();
        resetCreationIndex();

        OnEntityCreated = null;
        OnEntityWillBeDestroyed = null;
        OnEntityDestroyed = null;
        OnGroupCreated = null;
        OnGroupCleared = null;

    }


    public void updateGroupsComponentAddedOrRemoved(Entity entity, int index, IComponent component) throws EntityIndexException {
       Array<Group> groups = _groupsForIndex[index];
        if (groups != null) {
            Array<GroupChanged> events = EntitasCache.getGroupChangedList();
            for (int i = 0, groupsCount = groups.size; i < groupsCount; i++) {
                events.add(groups.get(i).handleEntity(entity));
            }
            for(int i = 0; i < events.size; i++) {
                GroupChanged groupChangedEvent = events.get(i);
                if (groupChangedEvent != null) {
                    groupChangedEvent.groupChanged(groups.get(i), entity, index, component);
                }
            }
            EntitasCache.pushGroupChangedList(events);
        }

    }

    protected void updateGroupsComponentReplaced(Entity entity, int index, IComponent previousComponent, IComponent newComponent) throws EntityIndexException {
        Array<Group> groups = _groupsForIndex[index];
        if (groups != null) {
            for (Group g : groups) {
                g.updateEntity(entity, index, previousComponent, newComponent);
            }
        }

    }

    protected void onEntityReleased(Entity entity) {
        if (entity.isEnabled()) {
            throw new EntityIsNotDestroyedException("Cannot release entity.");
        }
        entity.removeAllOnEntityReleasedHandlers();
        _retainedEntities.remove(entity);
        _reusableEntities.push(entity);
    }


    public Stack<IComponent>[] getComponentPools() {
         return _componentPools;
    }

    public PoolMetaData getMetaData() { return _metaData; }

    public int getCount() {
        return _entities.size;
    }

    public int getReusableEntitiesCount() {
        return _reusableEntities.size();
    }

    public int getRetainedEntitiesCount() {
        return _retainedEntities.size;
    }
}
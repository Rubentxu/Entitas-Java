package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.events.Event;
import com.ilargia.games.entitas.events.EventSource;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.exceptions.EntityIsNotDestroyedException;
import com.ilargia.games.entitas.exceptions.PoolDoesNotContainEntityException;
import com.ilargia.games.entitas.exceptions.PoolStillHasRetainedEntitiesException;
import com.ilargia.games.entitas.interfaces.*;

import java.util.*;

public class Pool {

    public Event<PoolChanged> OnEntityCreated;
    public Event<PoolChanged> OnEntityWillBeDestroyed;
    public Event<PoolChanged> OnEntityDestroyed;
    public Event<PoolGroupChanged> OnGroupCreated;
    public Event<PoolGroupChanged> OnGroupCleared;
    public Event<EntityChanged> _cachedEntityChanged;
    public Event<ComponentReplaced> _cachedComponentReplaced;
    public Event<EntityReleased> _cachedEntityReleased;

    private int _totalComponents;
    private int _creationIndex;
    private ObjectSet<Entity> _entities;
    private Stack<Entity> _reusableEntities;
    private ObjectSet<Entity> _retainedEntities;
    private ObjectSet<Entity> _entitiesCache;
    private PoolMetaData _metaData;
    protected ObjectMap<IMatcher, Group> _groups;
    protected Array<Group>[] _groupsForIndex;
    private Stack<IComponent>[] _componentPools;
    private ObjectMap<String, IEntityIndex> _entityIndices;
    private EventSource _source;


    public Pool(int totalComponents, EventSource source) {
        this(totalComponents, 0, null, source);
    }

    public Pool(int totalComponents, int startCreationIndex, PoolMetaData metaData, EventSource source) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _source = source;

        if(metaData != null) {
            _metaData = metaData;

            if(metaData.componentNames.Length != totalComponents) {
                throw new PoolMetaDataException(this, metaData);
            }
        } else {
            String[] componentNames = new String[totalComponents];
                const String prefix = "Index ";
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



        _reusableEntities = new Stack<>();
        _retainedEntities = new ObjectSet<>();
        _entities = new ObjectSet<>();
        _groups = new ObjectMap<>();

    }



    public Entity createEntity() throws EntityIndexException {
        Entity entity = _reusableEntities.size() > 0 ? _reusableEntities.pop() : new Entity(_totalComponents, _componentPools, _metaData);
        entity.setEnabled(true);
        entity.setCreationIndex(_creationIndex++);
        entity.retain(this);
        _entities.add(entity);
        _entitiesCache = null;

        for (EntityChanged entityChanged: _source.OnComponentAdded.listeners()) {
            entityChanged.entityChanged();
        }

        ((Entity ent, int index, IComponent component)-> {
            updateGroupsComponentAddedOrRemoved(ent);
        });
        entity.OnComponentRemoved.addListener(_cachedUpdateGroupsComponentAddedOrRemoved);
        entity.OnComponentReplaced.addListener(_cachedUpdateGroupsComponentReplaced);
        entity.OnEntityReleased.addListener("_cachedOnEntityReleased",_cachedOnEntityReleased);

        if (OnEntityCreated != null) {
            for (PoolChanged listener : OnEntityCreated.listeners()) {
                listener.poolChanged(this, entity);
            }
        }
        return entity;

    }

    public void destroyEntity(Entity entity) {
        if (!_entities.remove(entity)) {
            throw new PoolDoesNotContainEntityException(entity, "Could not destroy entity!");
        }
        _entitiesCache = null;

        if (OnEntityWillBeDestroyed != null) {
            for (PoolChanged listener : OnEntityWillBeDestroyed.listeners()) {
                listener.poolChanged(this, entity);
            }
        }

        entity.destroy();

        if (OnEntityDestroyed != null) {
            for (PoolChanged listener : OnEntityDestroyed.listeners()) {
                listener.poolChanged(this, entity);
            }
        }

        if (entity.getRetainCount() == 1) {
            entity.OnEntityReleased.removeListener("_cachedOnEntityReleased");
            _reusableEntities.push(entity);
        } else {
            _retainedEntities.add(entity);
        }
        entity.release(this);

    }

    public void destroyAllEntities() {
        for (Entity entity : getEntities()) {
            destroyEntity(entity);
        }
        _entities.clear();

        if (_retainedEntities.size != 0) {
            throw new PoolStillHasRetainedEntitiesException();
        }

    }

    public boolean hasEntity(Entity entity) {
        return _entities.contains(entity);
    }

    public ObjectSet<Entity> getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = new ObjectSet<Entity>(_entities.size);
            _entitiesCache = _entities;
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

            for (int index : matcher.getindices().items) {
                if (_groupsForIndex.get(index) == null) {
                    _groupsForIndex.insert(index, new Array<Group>());
                }
                _groupsForIndex.get(index).add(group);
            }

            if (OnGroupCreated != null) {
                for (PoolGroupChanged listener : OnGroupCreated.listeners()) {
                    listener.groupChanged(this, group);
                }
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
                for (PoolGroupChanged listener : OnGroupCleared.listeners()) {
                    listener.groupChanged(this, group);
                }
            }
        }
        _groups.clear();

        for (int i = 0, groupsForIndexLength = _groupsForIndex.size; i < groupsForIndexLength; i++) {
            _groupsForIndex.set(i, null);
        }
    }

    public void resetCreationIndex() {
        _creationIndex = 0;
    }

    protected void updateGroupsComponentAddedOrRemoved(Entity entity, int index, IComponent component) throws EntityIndexException {
       Array<Group> groups = _groupsForIndex[index];
        if (groups != null) {
            ArrayList<Event<GroupChanged>> events = new ArrayList<Event<GroupChanged>>();
            for (int i = 0, groupsCount = groups.size; i < groupsCount; i++) {
                events.add(groups.get(i).handleEntity(entity));
            }
            for (int i = 0, eventsCount = events.size(); i < eventsCount; i++) {
                Event<GroupChanged> groupChangedEvent = events.get(i);
                if (groupChangedEvent != null) {
                    //groupChangedEvent(groups[i], entity, index, component);
                    for (GroupChanged gc : groupChangedEvent.listeners())
                        gc.groupChanged(groups.get(i), entity, index, component);
                }
            }
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
        entity.OnEntityReleased.removeListener("_cachedOnEntityReleased");
        System.out.println("OnEntityReleased");
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
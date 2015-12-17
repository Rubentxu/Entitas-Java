package com.ilargia.games.entitas;

import com.ilargia.games.entitas.events.Event;
import com.ilargia.games.entitas.exceptions.EntityIsNotDestroyedException;
import com.ilargia.games.entitas.exceptions.PoolDoesNotContainEntityException;
import com.ilargia.games.entitas.exceptions.PoolStillHasRetainedEntitiesException;
import com.ilargia.games.entitas.interfaces.*;

import java.util.*;

public class Pool {

    private final Stack<Entity> _reusableEntities = new Stack<>();
    private final HashSet<Entity> _retainedEntities = new HashSet<>();
    private int _totalComponents;
    private int _creationIndex;
    private Set<Entity> _entitiesCache;
    private EntityChanged _cachedUpdateGroupsComponentAddedOrRemoved;
    private ComponentReplaced _cachedUpdateGroupsComponentReplaced;
    private EntityReleased _cachedOnEntityReleased;

    protected final HashSet<Entity> _entities = new HashSet<>();
    protected final HashMap<IMatcher, Group> _groups = new HashMap<>();
    protected ArrayList<ArrayList<Group>> _groupsForIndex;

    public Event<PoolChanged> OnEntityCreated = new Event<PoolChanged>();
    public Event<PoolChanged> OnEntityWillBeDestroyed = new Event<PoolChanged>();
    public Event<PoolChanged> OnEntityDestroyed = new Event<PoolChanged>();
    public Event<PoolGroupChanged> OnGroupCreated = new Event<PoolGroupChanged>();
    public Event<PoolGroupChanged> OnGroupCleared = new Event<PoolGroupChanged>();


    public Pool(int totalComponents) {
        this(totalComponents, 0);
    }

    public Pool(int totalComponents, int startCreationIndex) {
        _totalComponents = totalComponents;
        _creationIndex = startCreationIndex;
        _groupsForIndex = new ArrayList<ArrayList<Group>>(totalComponents);

        // Cache delegates to avoid gc allocations
        _cachedUpdateGroupsComponentAddedOrRemoved = (Entity entity, int index, IComponent component)
                                                        -> updateGroupsComponentAddedOrRemoved(entity, index, component);

        _cachedUpdateGroupsComponentReplaced = (Entity entity, int index, IComponent previousComponent, IComponent newComponent)
                                                    -> updateGroupsComponentReplaced(entity, index, previousComponent, newComponent);

        _cachedOnEntityReleased = (Entity entity) -> onEntityReleased(entity);
    }

    public int gettotalComponents() {
        return _totalComponents;
    }

    public int getcount() {
        return _entities.size();
    }

    public int getreusableEntitiesCount() {
        return _reusableEntities.size();
    }

    public int getretainedEntitiesCount() {
        return _retainedEntities.size();
    }

    public Entity createEntity() {
        Entity entity = _reusableEntities.size() > 0 ? _reusableEntities.pop() : new Entity(_totalComponents);
        entity.setEnabled(true);
        entity.setCreationIndex(_creationIndex++);
        entity.retain(this);
        _entities.add(entity);
        _entitiesCache = null;
        entity.OnComponentAdded.addListener(_cachedUpdateGroupsComponentAddedOrRemoved);
        entity.OnComponentRemoved.addListener(_cachedUpdateGroupsComponentAddedOrRemoved);
        entity.OnComponentReplaced.addListener(_cachedUpdateGroupsComponentReplaced);
        entity.OnEntityReleased.addListener(_cachedOnEntityReleased);

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

        if (_retainedEntities.size() != 0) {
            throw new PoolStillHasRetainedEntitiesException();
        }

    }

    public boolean hasEntity(Entity entity) {
        return _entities.contains(entity);
    }

    public Set<Entity> getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = new HashSet<Entity>(_entities.size());
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

            for (int index : matcher.getindices()) {
                if (_groupsForIndex.get(index) == null) {
                    _groupsForIndex.add(index, new ArrayList<Group>());
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

        for (int i = 0, groupsForIndexLength = _groupsForIndex.size(); i < groupsForIndexLength; i++) {
            _groupsForIndex.set(i, null);
        }
    }

    public void resetCreationIndex() {
        _creationIndex = 0;
    }

    protected void updateGroupsComponentAddedOrRemoved(Entity entity, int index, IComponent component) {
        ArrayList<Group> groups = _groupsForIndex.get(index);
        if (groups != null) {
            ArrayList<Event<GroupChanged>> events = new ArrayList<Event<GroupChanged>>();
            for (int i = 0, groupsCount = groups.size(); i < groupsCount; i++) {
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

    protected void updateGroupsComponentReplaced(Entity entity, int index, IComponent previousComponent, IComponent newComponent) {
        ArrayList<Group> groups = _groupsForIndex.get(index);
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
        _retainedEntities.remove(entity);
        _reusableEntities.push(entity);
    }

}
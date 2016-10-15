package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.events.Event;
import com.ilargia.games.entitas.exceptions.SingleEntityException;
import com.ilargia.games.entitas.interfaces.GroupChanged;
import com.ilargia.games.entitas.interfaces.GroupUpdated;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.IMatcher;

import java.util.HashSet;
import java.util.Iterator;

public class Group {

    private final ObjectSet<Entity> _entities = new ObjectSet<Entity>();
    public Event<GroupChanged> OnEntityAdded = new Event<GroupChanged>();
    public Event<GroupChanged> OnEntityRemoved = new Event<GroupChanged>();
    public Event<GroupUpdated> OnEntityUpdated = new Event<GroupUpdated>();
    private IMatcher _matcher;
    private Array<Entity> _entitiesCache;
    private Entity _singleEntityCache;
    private String _toStringCache;


    public Group(IMatcher matcher) {
        _matcher = matcher;
    }

    public int getcount() {
        return _entities.size;
    }

    public IMatcher getmatcher() {
        return _matcher;
    }

    public void handleEntitySilently(Entity entity) {
        if (_matcher.matches(entity)) {
            addEntitySilently(entity);
        } else {
            removeEntitySilently(entity);
        }
    }

    public void handleEntity(Entity entity, int index, IComponent component) {
        if (_matcher.matches(entity)) {
            addEntity(entity, index, component);
        } else {
            removeEntity(entity, index, component);
        }
    }

    public Event<GroupChanged> handleEntity(Entity entity) {
        return _matcher.matches(entity) ? addEntity(entity) : removeEntity(entity);

    }

    public void updateEntity(Entity entity, int index, IComponent previousComponent, IComponent newComponent) {
        if (_entities.contains(entity)) {
            if (OnEntityRemoved != null) {
                for (GroupChanged listener : OnEntityRemoved.listeners()) {
                    listener.groupChanged(this, entity, index, previousComponent);
                }
            }
            if (OnEntityAdded != null) {
                for (GroupChanged listener : OnEntityAdded.listeners()) {
                    listener.groupChanged(this, entity, index, newComponent);
                }
            }
            if (OnEntityUpdated != null) {
                for (GroupUpdated listener : OnEntityUpdated.listeners()) {
                    listener.groupUpdated(this, entity, index, previousComponent, newComponent);
                }
            }
        }
    }

    public void removeAllEventHandlers() {
        OnEntityAdded = null;
        OnEntityRemoved = null;
        OnEntityUpdated = null;
    }

    private boolean addEntitySilently(Entity entity) {
        boolean added = _entities.add(entity);
        if (added) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.retain(this);
        }
        return added;
    }

    private void addEntity(Entity entity, int index, IComponent component) {
        if (addEntitySilently(entity) && OnEntityAdded != null) {
            for (GroupChanged listener : OnEntityAdded.listeners()) {
                listener.groupChanged(this, entity, index, component);
            }
        }
    }

    private Event<GroupChanged> addEntity(Entity entity) {
        return addEntitySilently(entity) ? OnEntityAdded : null;
    }

    private boolean removeEntitySilently(Entity entity) {
        boolean removed = _entities.remove(entity);
        if (removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.release(this);
        }

        return removed;
    }

    private void removeEntity(Entity entity, int index, IComponent component) {
        boolean removed = _entities.remove(entity);
        if (removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            if (OnEntityRemoved != null) {
                for (GroupChanged listener : OnEntityRemoved.listeners()) {
                    listener.groupChanged(this, entity, index, component);
                }
            }
            entity.release(this);
        }
    }

    private Event<GroupChanged> removeEntity(Entity entity) {
        return removeEntitySilently(entity) ? OnEntityRemoved : null;
    }

    public boolean containsEntity(Entity entity) {
        return _entities.contains(entity);
    }

    public Array<Entity> getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = new Array<Entity>(false,_entities.size);
            for (Entity e : _entities) {
                _entitiesCache.add(e);
            }
        }
        return _entitiesCache;
    }

    public Entity getSingleEntity() {
        if (_singleEntityCache == null) {
            int c = _entities.size;
            if (c == 1) {
                Iterator<Entity> enumerator = _entities.iterator();
                _singleEntityCache = enumerator.next();
            } else if (c == 0) {
                return null;
            } else {
                throw new SingleEntityException(_matcher);
            }
        }
        return _singleEntityCache;
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            _toStringCache = "Group(" + _matcher + ")";
        }
        return _toStringCache;
    }

}
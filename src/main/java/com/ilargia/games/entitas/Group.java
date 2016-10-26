package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.exceptions.GroupSingleEntityException;
import com.ilargia.games.entitas.interfaces.GroupChanged;
import com.ilargia.games.entitas.interfaces.GroupUpdated;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.IMatcher;

import java.util.Iterator;

public class Group {

    private final ObjectSet<Entity> _entities = new ObjectSet<Entity>();
    public GroupChanged OnEntityAdded;
    public GroupChanged OnEntityRemoved;
    public GroupUpdated OnEntityUpdated;
    private IMatcher _matcher;
    private Array<Entity> _entitiesCache;
    private Entity _singleEntityCache;
    private String _toStringCache;


    public Group(IMatcher matcher) {
        _matcher = matcher;
    }

    public void handleEntitySilently(Entity entity) {
        if (_matcher.matches(entity)) {
            addEntitySilently(entity);
        } else {
            removeEntitySilently(entity);
        }
    }

    public void handleEntity(Entity entity, int index, IComponent component) throws EntityIndexException {
        if (_matcher.matches(entity)) {
            addEntity(entity, index, component);
        } else {
            removeEntity(entity, index, component);
        }
    }


    public void updateEntity(Entity entity, int index, IComponent previousComponent, IComponent newComponent) throws EntityIndexException {
        if (_entities.contains(entity)) {
            if (OnEntityRemoved != null) {
                OnEntityRemoved.groupChanged(this, entity, index, previousComponent);
            }
            if (OnEntityAdded != null) {
                OnEntityAdded.groupChanged(this, entity, index, previousComponent);
            }
            if (OnEntityUpdated != null) {
                OnEntityUpdated.groupUpdated(this, entity, index, previousComponent, newComponent);
            }
        }
    }

    public void removeAllEventHandlers() {
        OnEntityAdded = null;
        OnEntityRemoved = null;
        OnEntityUpdated = null;
    }

    public GroupChanged handleEntity(Entity entity) {
        return _matcher.matches(entity)
                ? (addEntitySilently(entity) ? OnEntityAdded : null)
                : (removeEntitySilently(entity) ? OnEntityRemoved : null);

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
            OnEntityAdded.groupChanged(this, entity, index, component);
        }
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
                OnEntityRemoved.groupChanged(this, entity, index, component);
            }
            entity.release(this);
        }
    }

    public boolean containsEntity(Entity entity) {
        return _entities.contains(entity);
    }

    public Entity[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = new Array<Entity>(false, _entities.size);
            for (Entity e : _entities) {
                _entitiesCache.add(e);
            }
        }
        return _entitiesCache.items;
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
                throw new GroupSingleEntityException(this);
            }
        }
        return _singleEntityCache;
    }


    public int getcount() {
        return _entities.size;
    }

    public IMatcher getmatcher() {
        return _matcher;
    }


    @Override
    public String toString() {
        if (_toStringCache == null) {
            _toStringCache = "Group(" + _matcher + ")";
        }
        return _toStringCache;
    }

}
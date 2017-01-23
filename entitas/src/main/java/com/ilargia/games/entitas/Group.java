package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.exceptions.GroupSingleEntityException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import java.util.Iterator;
import java.util.Set;

public class Group<E extends IEntity> {

    private final Set<E> _entities; // ObjectOpenHashSet
    private final EventBus _eventBus;
    public Class<E> type;
    private IMatcher _matcher;
    private E[] _entitiesCache;
    private E _singleEntityCache;
    private String _toStringCache;


    public Group(IMatcher matcher, Class<E> clazz, EventBus eventBus) {
        _entities = Collections.createSet(Entity.class);
        _matcher = matcher;
        type = clazz;
        _eventBus = eventBus;

    }


    public void updateEntity(E entity, int index, IComponent previousComponent, IComponent newComponent) throws EntityIndexException {
        if (_entities.contains(entity)) {
            _eventBus.notifyOnEntityRemoved(this, entity, index, previousComponent);
            _eventBus.notifyOnEntityAdded(this, entity, index, previousComponent);
            _eventBus.notifyOnEntityUpdated(this, entity, index, previousComponent, newComponent);
        }
    }

    public void removeAllEventHandlers() {
        _eventBus.OnEntityAdded(this).clear();
        _eventBus.OnEntityRemoved(this).clear();
        _eventBus.OnEntityUpdated(this).clear();
    }

    public void handleEntitySilently(E entity) {
        if (_matcher.matches(entity)) {
            addEntitySilently(entity);
        } else {
            removeEntitySilently(entity);
        }
    }

    public void handleEntity(E entity, int index, IComponent component) {
        if(_matcher.matches(entity)) {
            addEntity(entity, index, component);
        } else {
            removeEntity(entity, index, component);
        }

    }

    private boolean addEntitySilently(E entity) {
        boolean added = _entities.add(entity);
        if (added) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.retain(this);
        }
        return added;

    }

    void addEntity(E entity, int index, IComponent component) {
        if(addEntitySilently(entity)) {
            _eventBus.notifyOnEntityAdded(this, entity, index, component);
        }

    }

    private boolean removeEntitySilently(E entity) {
        boolean removed = _entities.remove(entity);
        if (removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.release(this);
        }

        return removed;
    }

    void removeEntity(Entity entity, int index, IComponent component) {
        boolean removed = _entities.remove(entity);
        if(removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            _eventBus.notifyOnEntityRemoved(this, entity, index, component);
            entity.release(this);
        }

    }


    public boolean containsEntity(E entity) {
        return _entities.contains(entity);
    }

    public E[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (E[]) java.lang.reflect.Array.newInstance(type, _entities.size());
            int i = 0;
            for (E entity : _entities) {
                _entitiesCache[i] = entity;
                i++;
            }

        }
        return _entitiesCache;

    }


    public E getSingleEntity() {
        if (_singleEntityCache == null) {
            int c = _entities.size();
            if (c == 1) {
                Iterator<E> enumerator = _entities.iterator();
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
        return _entities.size();
    }

    public IMatcher getMatcher() {
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
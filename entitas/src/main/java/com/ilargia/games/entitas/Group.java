package com.ilargia.games.entitas;

import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.exceptions.GroupSingleEntityException;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.IMatcher;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;
import com.ilargia.games.entitas.interfaces.events.GroupUpdated;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Iterator;

public class Group<E extends Entity> {

    private final ObjectOpenHashSet<E> _entities;
    public GroupChanged<E> OnEntityAdded;
    public GroupChanged<E> OnEntityRemoved;
    public GroupUpdated<E> OnEntityUpdated;
    public Class<E> type;
    private IMatcher _matcher;
    private E[] _entitiesCache;
    private E _singleEntityCache;
    private String _toStringCache;


    public Group(IMatcher matcher, Class<E> clazz) {
        _entities = new ObjectOpenHashSet<E>();
        _matcher = matcher;
        type = clazz;
    }


    public void updateEntity(E entity, int index, IComponent previousComponent, IComponent newComponent) throws EntityIndexException {
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

    public void handleEntitySilently(E entity) {
        if (_matcher.matches(entity)) {
            addEntitySilently(entity);
        } else {
            removeEntitySilently(entity);
        }
    }

    public GroupChanged handleEntity(E entity) {
        return _matcher.matches(entity)
                ? (addEntitySilently(entity) ? OnEntityAdded : null)
                : (removeEntitySilently(entity) ? OnEntityRemoved : null);

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

    private boolean removeEntitySilently(E entity) {
        boolean removed = _entities.remove(entity);
        if (removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.release(this);
        }

        return removed;
    }


    public boolean containsEntity(E entity) {
        return _entities.contains(entity);
    }

    public E[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (E[]) new Entity [_entities.size()];
            _entities.toArray(_entitiesCache);
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
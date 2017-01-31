package com.ilargia.games.entitas.group;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.events.GroupUpdated;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.GroupSingleEntityException;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Iterator;
import java.util.Set;

public class Group<TEntity extends IEntity> implements IGroup<TEntity> {

    public Event<GroupChanged<TEntity>> OnEntityAdded;
    public Event<GroupChanged<TEntity>> OnEntityRemoved;
    public Event<GroupUpdated<TEntity>> OnEntityUpdated;

    private IMatcher<TEntity> _matcher;
    private final Set<TEntity> _entities; //
    private TEntity[] _entitiesCache;
    private TEntity _singleEntityCache;
    private String _toStringCache;
    public Class<TEntity> type;


    @Override
    public int getCount() {
        return _entities.size();
    }

    @Override
    public IMatcher getMatcher() {
        return _matcher;
    }


    public Group(IMatcher<TEntity> matcher, Class<TEntity> clazz) {
        _entities = Collections.createSet(Entity.class);
        _matcher = matcher;
        type = clazz;
        OnEntityAdded = new Event();
        OnEntityRemoved = new Event();
        OnEntityUpdated = new Event();

    }

    public void handleEntitySilently(TEntity entity) {
        if (_matcher.matches(entity)) {
            addEntitySilently(entity);
        } else {
            removeEntitySilently(entity);
        }
    }

    public void handleEntity(TEntity entity, int index, IComponent component) {
        if (_matcher.matches(entity)) {
            addEntitySilently(entity);
        } else {
            removeEntitySilently(entity);
        }

    }

    public void updateEntity(TEntity entity, int index, IComponent previousComponent, IComponent newComponent) {
        if (_entities.contains(entity)) {
            notifyOnEntityRemoved(this, entity, index, previousComponent);
            notifyOnEntityAdded(this, entity, index, previousComponent);
            notifyOnEntityUpdated(this, entity, index, previousComponent, newComponent);
        }
    }

    public void removeAllEventHandlers() {
        OnEntityAdded.clear();
        OnEntityRemoved.clear();
        OnEntityUpdated.clear();
    }

    @Override
    public Event<GroupChanged<TEntity>> handleEntity(TEntity entity) {
        return (_matcher.matches(entity))
                ? (addEntitySilently(entity)) ? OnEntityAdded : null
                : (removeEntitySilently(entity)) ? OnEntityRemoved : null;

    }

    private boolean addEntitySilently(TEntity entity) {
        boolean added = _entities.add(entity);
        if (added) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.retain(this);
        }
        return added;

    }

    void addEntity(TEntity entity, int index, IComponent component) {
        if (addEntitySilently(entity)) {
            notifyOnEntityAdded(this, entity, index, component);
        }

    }

    private boolean removeEntitySilently(TEntity entity) {
        boolean removed = _entities.remove(entity);
        if (removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            entity.release(this);
        }

        return removed;
    }

    void removeEntity(TEntity entity, int index, IComponent component) {
        boolean removed = _entities.remove(entity);
        if (removed) {
            _entitiesCache = null;
            _singleEntityCache = null;
            notifyOnEntityRemoved(this, entity, index, component);
            entity.release(this);
        }

    }

    public boolean containsEntity(TEntity entity) {
        return _entities.contains(entity);
    }

    public TEntity[] getEntities() {
        if (_entitiesCache == null) {
            _entitiesCache = (TEntity[]) java.lang.reflect.Array.newInstance(type, _entities.size());
            int i = 0;
            for (TEntity entity : _entities) {
                _entitiesCache[i] = entity;
                i++;
            }

        }
        return _entitiesCache;

    }

    public TEntity getSingleEntity() {
        if (_singleEntityCache == null) {
            int c = _entities.size();
            if (c == 1) {
                Iterator<TEntity> enumerator = _entities.iterator();
                _singleEntityCache = enumerator.next();
            } else if (c == 0) {
                return null;
            } else {
                throw new GroupSingleEntityException(this);
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

    public static <TE extends Entity> Collector<TE> createCollector(IGroup<TE> group, GroupEvent groupEvent) {
        return new Collector<TE>(group, groupEvent);
    }

    public void notifyOnEntityAdded(IGroup<TEntity> group, TEntity entity, int index, IComponent component) {
        for (GroupChanged<TEntity> listener : OnEntityAdded.listeners()) {
            listener.changed(group, entity, index, component);
        }
    }

    public void notifyOnEntityRemoved(IGroup<TEntity> group, TEntity entity, int index, IComponent component) {
        for (GroupChanged<TEntity> listener : OnEntityRemoved.listeners()) {
            listener.changed(group, entity, index, component);
        }
    }

    public void notifyOnEntityUpdated(IGroup<TEntity> group, TEntity entity, int index, IComponent component, IComponent newComponent) {
        for (GroupUpdated<TEntity> listener : OnEntityUpdated.listeners()) {
            listener.updated(group, entity, index, component, newComponent);
        }
    }


    public void clearEventsPool() {
        OnEntityAdded.clear();
        OnEntityRemoved.clear();
        OnEntityUpdated.clear();

    }

}
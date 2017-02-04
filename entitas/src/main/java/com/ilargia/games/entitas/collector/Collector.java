package com.ilargia.games.entitas.collector;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.EntityCollectorException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.group.Group;

import java.util.Arrays;
import java.util.Set;

public class Collector<TEntity extends IEntity> {

    public Set<TEntity> _collectedEntities; //ObjectOpenHashSet
    private IGroup<TEntity>[] _groups;
    private GroupEvent[] _groupEvents;
    GroupChanged<TEntity> _addEntityCache;
    String _toStringCache;
    StringBuilder _toStringBuilder;


    public Collector(IGroup<TEntity> group, GroupEvent eventType) {
        this(new IGroup[]{group}, new GroupEvent[]{eventType});

    }

    public Collector(IGroup<TEntity>[] groups, GroupEvent[] groupEvents) {
        _groups = groups;
        _collectedEntities = Collections.createSet(IEntity.class);
        _groupEvents = groupEvents;

        if (groups.length != groupEvents.length) {
            throw new EntityCollectorException("Unbalanced count with groups (" + groups.length +
                    ") and event types (" + groupEvents.length + ").",
                    "Group and event type count must be equal."
            );
        }

        _addEntityCache = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
            addEntity(group, entity, index, component);
        };
        activate();
    }

    public void activate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = (Group) _groups[i];
            GroupEvent groupEvent = _groupEvents[i];
            switch (groupEvent) {
                case Added:
                    group.OnEntityAdded(_addEntityCache);
                    break;
                case Removed:
                    group.OnEntityRemoved(_addEntityCache);
                    break;
                case AddedOrRemoved:
                    group.OnEntityAdded(_addEntityCache);
                    group.OnEntityRemoved(_addEntityCache);
                    break;
            }

        }
    }

    public void deactivate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = (Group) _groups[i];
            group.OnEntityAdded.clear();
            group.OnEntityRemoved.clear();

        }
        clearCollectedEntities();
    }

    public void clearCollectedEntities() {
        for (IEntity entity : _collectedEntities) {
            entity.release(this);
        }
        _collectedEntities.clear();

    }

    void addEntity(IGroup<TEntity> group, TEntity entity, int index, IComponent component) {
        boolean added = _collectedEntities.add(entity);
        if (added) {
            entity.retain(this);
        }
    }

    @Override
    public String toString() {
        return "Collector{" +
                "_collectedEntities=" + _collectedEntities +
                ", _groups=" + Arrays.toString(_groups) +
                ", _groupEvents=" + Arrays.toString(_groupEvents) +
                ", _addEntityCache=" + _addEntityCache +
                ", _toStringCache='" + _toStringCache + '\'' +
                ", _toStringBuilder=" + _toStringBuilder +
                '}';
    }
}

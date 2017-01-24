package com.ilargia.games.entitas.collector;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.EntityCollectorException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.group.Group;

import java.util.Set;

import static com.ilargia.games.entitas.events.GroupEvent.*;

public class Collector<TEntity extends IEntity> {

    private final EventBus<TEntity> _eventBus;
    public Set<TEntity> _collectedEntities; //ObjectOpenHashSet
    private IGroup<TEntity>[] _groups;
    private GroupEvent[] _groupEvents;
    GroupChanged<TEntity> _addEntityCache;
    String _toStringCache;
    StringBuilder _toStringBuilder;


    public Collector(IGroup<TEntity> group, GroupEvent eventType, EventBus<TEntity> eventBus) {
        this(new IGroup[]{group}, new GroupEvent[]{eventType}, eventBus);

    }

    public Collector(IGroup<TEntity>[] groups, GroupEvent[] groupEvents, EventBus<TEntity> eventBus) {
        _groups = groups;
        _collectedEntities = Collections.createSet(Entity.class);
        _groupEvents = groupEvents;
        _eventBus = eventBus;

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
            IGroup group = _groups[i];
            GroupEvent groupEvent = _groupEvents[i];
            switch (groupEvent) {
                case Added:
                    _eventBus.OnEntityAdded.addListener(group,_addEntityCache);
                    break;
                case Removed:
                    _eventBus.OnEntityRemoved.addListener(group,_addEntityCache);
                    break;
                case AddedOrRemoved:
                    _eventBus.OnEntityAdded.addListener(group,_addEntityCache);
                    _eventBus.OnEntityRemoved .addListener(group,_addEntityCache);
                    break;
            }

        }
    }

    public void deactivate() {
        for (int i = 0; i < _groups.length; i++) {
            IGroup group = _groups[i];
            _eventBus.OnEntityAdded.removeListener(group);
            _eventBus.OnEntityRemoved.removeListener(group);

        }
        clearCollectedEntities();
    }

    public void clearCollectedEntities() {
        for (IEntity entity : _collectedEntities) {
            entity.release(this);
        }
        _collectedEntities.clear();

    }

    void addEntity(IGroup group, TEntity entity, int index, IComponent component) {
        boolean added = _collectedEntities.add(entity);
        if (added) {
            entity.retain(this);
        }
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            if (_toStringBuilder == null) {
                _toStringBuilder = new StringBuilder();
            }
            _toStringBuilder.append("Collector(");

            String separator = ", ";
            int lastSeparator = _groups.length - 1;
            for (int i = 0; i < _groups.length; i++) {
                _toStringBuilder.append(_groups[i]);
                if (i < lastSeparator) {
                    _toStringBuilder.append(separator);
                }

            }

            _toStringBuilder.append(")");
            _toStringCache = _toStringBuilder.toString();
        }

        return _toStringCache;
    }


}

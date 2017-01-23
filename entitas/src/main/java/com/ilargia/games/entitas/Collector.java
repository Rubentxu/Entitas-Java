package com.ilargia.games.entitas;

import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.exceptions.EntityCollectorException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.events.GroupChanged;
import java.util.Set;

public class Collector<E extends Entity> {

    private final EventBus<E> _eventBus;
    public Set<E> _collectedEntities; //ObjectOpenHashSet
    private Group<E>[] _groups;
    private GroupEvent[] _groupEvents;
    GroupChanged<E> _addEntityCache;
    String _toStringCache;
    StringBuilder _toStringBuilder;


    public Collector(Group group, GroupEvent eventType, EventBus<E> eventBus) {
        this(new Group[]{group}, new GroupEvent[]{eventType}, eventBus);

    }

    public Collector(Group<E>[] groups, GroupEvent[] groupEvents, EventBus<E> eventBus) {
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

        _addEntityCache = (Group<E> group, E entity, int index, IComponent component) -> {
            addEntity(group, entity, index, component);
        };
        activate();
    }

    public void activate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = _groups[i];
            GroupEvent groupEvent = _groupEvents[i];
            if (groupEvent == GroupEvent.Added) {
                _eventBus.OnEntityAdded(group).addListener(_addEntityCache);

            } else if (groupEvent == GroupEvent.Removed) {
                _eventBus.OnEntityRemoved(group).addListener(_addEntityCache);

            } else if (groupEvent == GroupEvent.AddedOrRemoved) {
                _eventBus.OnEntityAdded(group).addListener(_addEntityCache);
                _eventBus.OnEntityRemoved(group).addListener(_addEntityCache);

            }
        }
    }

    public void deactivate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = _groups[i];
            _eventBus.OnEntityAdded(group).removeListener(_addEntityCache);
            _eventBus.OnEntityRemoved(group).removeListener(_addEntityCache);

        }
        clearCollectedEntities();
    }

    public void clearCollectedEntities() {
        for (Entity entity : _collectedEntities) {
            entity.release(this);
        }
        _collectedEntities.clear();

    }

    void addEntity(Group group, E entity, int index, IComponent component) {
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

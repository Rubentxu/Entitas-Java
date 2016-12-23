package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.exceptions.EntityCollectorException;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;
import com.ilargia.games.entitas.interfaces.IComponent;

public class EntityCollector<E extends Entity> {

    public ObjectSet<E> _collectedEntities;
    GroupChanged<E> _addEntityCache;
    String _toStringCache;
    StringBuilder _toStringBuilder;
    private Group<E>[] _groups;
    private GroupEventType[] _eventTypes;

    public EntityCollector(Group group, GroupEventType eventType) {
        this(new Group[]{group}, new GroupEventType[]{eventType});

    }

    public EntityCollector(Group<E>[] groups, GroupEventType[] eventTypes) throws EntityCollectorException {
        _groups = groups;
        _collectedEntities = new ObjectSet<>();
        _eventTypes = eventTypes;

        if (groups.length != eventTypes.length) {
            throw new EntityCollectorException("Unbalanced count with groups (" + groups.length +
                    ") and event types (" + eventTypes.length + ").",
                    "Group and event type count must be equal."
            );
        }

        _addEntityCache = (Group<E> group, E entity, int index, IComponent component) -> {
            boolean added = _collectedEntities.add(entity);
            if (added) {
                entity.retain(this);
            }
        };
        activate();
    }

    public void activate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = _groups[i];
            GroupEventType eventType = _eventTypes[i];
            if (eventType == GroupEventType.OnEntityAdded) {

                group.OnEntityAdded = _addEntityCache;

            } else if (eventType == GroupEventType.OnEntityRemoved) {
                group.OnEntityRemoved = _addEntityCache;

            } else if (eventType == GroupEventType.OnEntityAddedOrRemoved) {
                group.OnEntityAdded = _addEntityCache;
                group.OnEntityRemoved = _addEntityCache;

            }
        }
    }

    public void deactivate() {
        for (int i = 0; i < _groups.length; i++) {
            Group group = _groups[i];
            group.OnEntityAdded = _addEntityCache;

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

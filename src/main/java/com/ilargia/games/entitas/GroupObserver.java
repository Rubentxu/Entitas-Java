package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.exceptions.GroupObserverException;
import com.ilargia.games.entitas.interfaces.GroupChanged;
import com.ilargia.games.entitas.interfaces.IComponent;

import java.util.HashSet;
import java.util.Objects;

public class GroupObserver {
    private ObjectSet<Entity> _collectedEntities;
    private Group[] _groups;
    private GroupEventType[] _eventTypes;
    private GroupChanged _addEntityCache;


    public GroupObserver(Group group, GroupEventType eventType) {
        this(new Group[]{group}, new GroupEventType[]{eventType});
    }

    public GroupObserver(Group[] groups, GroupEventType[] eventTypes) {
        if (groups.length != eventTypes.length) {
            throw new GroupObserverException("Unbalanced count with groups (" + groups.length + ") and event types (" + eventTypes.length + ")");
        }

        _collectedEntities = new ObjectSet<Entity>();
        _groups = groups;
        _eventTypes = eventTypes;
        _addEntityCache = (Group group, Entity entity, int index, IComponent component) -> addEntity(group, entity, index, component);
        activate();
    }

    public ObjectSet<Entity> getcollectedEntities() {
        return _collectedEntities;
    }

    public void activate() {
        for (int i = 0, groupsLength = _groups.length; i < groupsLength; i++) {
            Group group = _groups[i];
            GroupEventType eventType = _eventTypes[i];
            if (eventType == GroupEventType.OnEntityAdded) {
                group.OnEntityAdded.removeListener("_addEntityCache");
                group.OnEntityAdded.addListener("_addEntityCache", (Group g, Entity entity, int index, IComponent component) -> _addEntityCache.groupChanged(g, entity, index, component));
            } else if (eventType == GroupEventType.OnEntityRemoved) {
                group.OnEntityRemoved.removeListener("_addEntityCache");
                group.OnEntityRemoved.addListener("_addEntityCache", (Group g, Entity entity, int index, IComponent component) -> _addEntityCache.groupChanged(g, entity, index, component));
            } else if (eventType == GroupEventType.OnEntityAddedOrRemoved) {
                group.OnEntityAdded.removeListener("_addEntityCache");
                group.OnEntityAdded.addListener("_addEntityCache", (Group g, Entity entity, int index, IComponent component) -> _addEntityCache.groupChanged(g, entity, index, component));
                group.OnEntityRemoved.removeListener("_addEntityCache");
                group.OnEntityRemoved.addListener("_addEntityCache", (Group g, Entity entity, int index, IComponent component) -> _addEntityCache.groupChanged(g, entity, index, component));
            }
        }
    }

    public void deactivate() {
        for (int i = 0, groupsLength = _groups.length; i < groupsLength; i++) {
            Group group = _groups[i];
            group.OnEntityAdded.removeListener("_addEntityCache");
            group.OnEntityRemoved.removeListener("_addEntityCache");
        }
        clearCollectedEntities();
    }

    public void clearCollectedEntities() {
        for (Entity entity : _collectedEntities) {
            entity.release(this);
        }
        _collectedEntities.clear();
    }

    private void addEntity(Group group, Entity entity, int index, IComponent component) {
        if (_collectedEntities.add(entity)) {
            entity.retain(this);
        }
    }

}
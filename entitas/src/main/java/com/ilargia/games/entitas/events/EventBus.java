package com.ilargia.games.entitas.events;

import com.ilargia.games.entitas.BaseContext;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.events.*;
import java.util.Map;

public class EventBus<E extends Entity> {

    public Event<ContextChanged<BaseContext, E>> OnEntityCreated;
    public Event<ContextChanged<BaseContext, E>> OnEntityWillBeDestroyed;
    public Event<ContextChanged<BaseContext, E>> OnEntityDestroyed;
    public Event<ContextGroupChanged<BaseContext>> OnGroupCreated;
    public Event<ContextGroupChanged<BaseContext>> OnGroupCleared;

    public Event<EntityChanged> OnComponentAdded;
    public Event<EntityChanged> OnComponentRemoved;
    public Event<ComponentReplaced> OnComponentReplaced;
    public Event<EntityReleased> OnEntityReleased;

    private Map<Group, Event<GroupChanged<E>>> OnEntityAdded;
    private Map<Group, Event<GroupChanged<E>>> OnEntityRemoved;
    private Map<Group, Event<GroupUpdated<E>>> OnEntityUpdated;

    public EventBus() {
        OnEntityCreated = new Event<>();
        OnEntityWillBeDestroyed = new Event<>();
        OnEntityDestroyed = new Event<>();
        OnGroupCreated = new Event<>();
        OnGroupCleared = new Event<>();
        OnComponentAdded = new Event<>();
        OnComponentRemoved = new Event<>();
        OnComponentReplaced = new Event<>();
        OnEntityReleased = new Event<>();
        OnEntityAdded = Collections.createMap(Group.class, GroupChanged.class);
        OnEntityRemoved = Collections.createMap(Group.class, GroupChanged.class);
        OnEntityUpdated = Collections.createMap(Group.class, GroupChanged.class);

    }

    public <P extends BaseContext> void notifyEntityCreated(P pool, E entity) {
        for (ContextChanged<BaseContext, E> listener : OnEntityCreated.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public <P extends BaseContext> void notifyEntityWillBeDestroyed(P pool, E entity) {
        for (ContextChanged<BaseContext, E> listener : OnEntityWillBeDestroyed.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public <P extends BaseContext> void notifyEntityDestroyed(P pool, E entity) {
        for (ContextChanged<BaseContext, E> listener : OnEntityDestroyed.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public <P extends BaseContext> void notifyGroupCreated(P pool, Group group) {
        for (ContextGroupChanged<BaseContext> listener : OnGroupCreated.listeners()) {
            listener.groupChanged(pool, group);
        }
    }

    public <P extends BaseContext> void notifyGroupCleared(P pool, Group group) {
        for (ContextGroupChanged<BaseContext> listener : OnGroupCleared.listeners()) {
            listener.groupChanged(pool, group);
        }
    }

    public void notifyComponentAdded(E entity, int index, IComponent component) {
        for (EntityChanged listener : OnComponentAdded.listeners()) {
            listener.entityChanged(entity, index, component);
        }
    }

    public void notifyComponentRemoved(E entity, int index, IComponent component) {
        for (EntityChanged listener : OnComponentRemoved.listeners()) {
            listener.entityChanged(entity, index, component);
        }
    }

    public void notifyComponentReplaced(E entity, int index, IComponent previousComponent, IComponent newComponent) {
        for (ComponentReplaced listener : OnComponentReplaced.listeners()) {
            listener.componentReplaced(entity, index, previousComponent, newComponent);
        }
    }

    public void notifyEntityReleased(E entity) {
        for (EntityReleased listener : OnEntityReleased.listeners()) {
            listener.entityReleased(entity);
        }
    }

    public Event<GroupChanged<E>> OnEntityAdded(Group<E> group) {
        Event<GroupChanged<E>> event = OnEntityAdded.get(group);
        if (event == null) {
            event = new Event<>();
        }
        return event;
    }

    public void notifyOnEntityAdded(Group<E> group, E entity, int index, IComponent component) {
        Event<GroupChanged<E>> events = OnEntityAdded.get(group);
        if (events != null) {
            for (GroupChanged<E> listener : OnEntityAdded.get(group).listeners()) {
                listener.groupChanged(group, entity, index, component);
            }
        }
    }

    public Event<GroupChanged<E>> OnEntityRemoved(Group<E> group) {
        Event<GroupChanged<E>> event = OnEntityRemoved.get(group);
        if (event == null) {
            event = new Event<>();
        }
        return event;
    }

    public void notifyOnEntityRemoved(Group<E> group, E entity, int index, IComponent component) {
        Event<GroupChanged<E>> events = OnEntityRemoved.get(group);
        if (events != null) {
            for (GroupChanged<E> listener : OnEntityRemoved.get(group).listeners()) {
                listener.groupChanged(group, entity, index, component);
            }
        }
    }

    public  Event<GroupUpdated<E>> OnEntityUpdated(Group<E> group) {
        Event<GroupUpdated<E>> event = OnEntityUpdated.get(group);
        if (event == null) {
            event = new Event<>();
        }
        return event;
    }

    public void notifyOnEntityUpdated(Group<E> group, E entity, int index, IComponent component, IComponent newComponent) {
        Event<GroupUpdated<E>> events = OnEntityUpdated.get(group);
        if (events != null) {
            for (GroupUpdated<E> listener : OnEntityUpdated.get(group).listeners()) {
                listener.groupUpdated(group, entity, index, component, newComponent);
            }
        }
    }


    public void clearEventsPool() {
        OnEntityCreated.clear();
        OnEntityWillBeDestroyed.clear();
        OnEntityDestroyed.clear();
        OnGroupCreated.clear();
        OnGroupCleared.clear();
        OnEntityAdded.clear();
        OnEntityRemoved.clear();
        OnEntityUpdated.clear();

    }



}

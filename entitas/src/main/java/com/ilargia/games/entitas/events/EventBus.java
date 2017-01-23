package com.ilargia.games.entitas.events;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.*;

public class EventBus<TEntity extends IEntity> {

    public Event<ContextEntityChanged> OnEntityCreated;
    public Event<ContextEntityChanged> OnEntityWillBeDestroyed;
    public Event<ContextEntityChanged> OnEntityDestroyed;
    public Event<ContextGroupChanged> OnGroupCreated;
    public Event<ContextGroupChanged> OnGroupCleared;

    public Event<EntityComponentChanged> OnComponentAdded;
    public Event<EntityComponentChanged> OnComponentRemoved;
    public Event<EntityComponentReplaced> OnComponentReplaced;
    public Event<EntityReleased> OnEntityReleased;

    private Event<GroupChanged<TEntity>> OnEntityAdded;
    private Event<GroupChanged<TEntity>> OnEntityRemoved;
    private Event<GroupUpdated<TEntity>> OnEntityUpdated;


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
        OnEntityAdded = new Event<>();
        OnEntityRemoved = new Event<>();
        OnEntityUpdated = new Event<>();

    }

    public void notifyEntityCreated(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityCreated.listeners()) {
            listener.changed(context, entity);
        }
    }

    public void notifyEntityWillBeDestroyed(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityWillBeDestroyed.listeners()) {
            listener.changed(context, entity);
        }
    }

    public void notifyEntityDestroyed(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityDestroyed.listeners()) {
            listener.changed(context, entity);
        }
    }

    public void notifyGroupCreated(IContext context, IGroup group) {
        for (ContextGroupChanged listener : OnGroupCreated.listeners()) {
            listener.changed(context, group);
        }
    }

    public void notifyGroupCleared(IContext context, IGroup group) {
        for (ContextGroupChanged listener : OnGroupCleared.listeners()) {
            listener.changed(context, group);
        }
    }

    public void notifyComponentAdded(IEntity entity, int index, IComponent component) {
        for (EntityComponentChanged listener : OnComponentAdded.listeners()) {
            listener.changed(entity, index, component);
        }
    }

    public void notifyComponentRemoved(IEntity entity, int index, IComponent component) {
        for (EntityComponentChanged listener : OnComponentRemoved.listeners()) {
            listener.changed(entity, index, component);
        }
    }

    public void notifyComponentReplaced(IEntity entity, int index, IComponent previousComponent, IComponent newComponent) {
        for (EntityComponentReplaced listener : OnComponentReplaced.listeners()) {
            listener.replaced(entity, index, previousComponent, newComponent);
        }
    }

    public void notifyEntityReleased(IEntity entity) {
        for (EntityReleased listener : OnEntityReleased.listeners()) {
            listener.released(entity);
        }
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

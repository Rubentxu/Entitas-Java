package com.ilargia.games.entitas.events;

import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.*;

public class EventBus<E extends Entity, P extends BasePool> {

    public Event<PoolChanged<P, E>> OnEntityCreated;
    public Event<PoolChanged<P, E>> OnEntityWillBeDestroyed;
    public Event<PoolChanged<P, E>> OnEntityDestroyed;
    public Event<PoolGroupChanged<P>> OnGroupCreated;
    public Event<PoolGroupChanged<P>> OnGroupCleared;

    public Event<EntityChanged> OnComponentAdded;
    public Event<EntityChanged> OnComponentRemoved;
    public Event<ComponentReplaced> OnComponentReplaced;
    public Event<EntityReleased> OnEntityReleased;

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
    }

    public void notifyEntityCreated(P pool, E entity) {
        for (PoolChanged<P, E> listener : OnEntityCreated.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public void notifyEntityWillBeDestroyed(P pool, E entity) {
        for (PoolChanged<P, E> listener : OnEntityWillBeDestroyed.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public void notifyEntityDestroyed(P pool, E entity) {
        for (PoolChanged<P, E> listener : OnEntityDestroyed.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public void notifyGroupCreated(P pool, Group group) {
        for (PoolGroupChanged<P> listener : OnGroupCreated.listeners()) {
            listener.groupChanged(pool, group);
        }
    }

    public void notifyGroupCleared(P pool, Group group) {
        for (PoolGroupChanged<P> listener : OnGroupCleared.listeners()) {
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

    public void clearEventsPool() {
        OnEntityCreated.clear();
        OnEntityWillBeDestroyed.clear();
        OnEntityDestroyed.clear();
        OnGroupCreated.clear();
        OnGroupCleared.clear();

    }


}

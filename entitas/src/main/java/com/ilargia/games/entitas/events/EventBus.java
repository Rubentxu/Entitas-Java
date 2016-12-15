package com.ilargia.games.entitas.events;

import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.*;

public class EventBus<E extends Entity> {

    public Event<PoolChanged<BasePool, E>> OnEntityCreated;
    public Event<PoolChanged<BasePool, E>> OnEntityWillBeDestroyed;
    public Event<PoolChanged<BasePool, E>> OnEntityDestroyed;
    public Event<PoolGroupChanged<BasePool>> OnGroupCreated;
    public Event<PoolGroupChanged<BasePool>> OnGroupCleared;

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

    public <P extends BasePool> void notifyEntityCreated(P pool, E entity) {
        for (PoolChanged<BasePool, E> listener : OnEntityCreated.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public <P extends BasePool> void notifyEntityWillBeDestroyed(P pool, E entity) {
        for (PoolChanged<BasePool, E> listener : OnEntityWillBeDestroyed.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public <P extends BasePool> void notifyEntityDestroyed(P pool, E entity) {
        for (PoolChanged<BasePool, E> listener : OnEntityDestroyed.listeners()) {
            listener.poolChanged(pool, entity);
        }
    }

    public <P extends BasePool> void notifyGroupCreated(P pool, Group group) {
        for (PoolGroupChanged<BasePool> listener : OnGroupCreated.listeners()) {
            listener.groupChanged(pool, group);
        }
    }

    public <P extends BasePool> void notifyGroupCleared(P pool, Group group) {
        for (PoolGroupChanged<BasePool> listener : OnGroupCleared.listeners()) {
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

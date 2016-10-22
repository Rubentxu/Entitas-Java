package com.ilargia.games.entitas.events;


import com.ilargia.games.entitas.interfaces.*;

public class EventSource {
    public Event<EntityChanged> OnComponentAdded;
    public Event<EntityChanged> OnComponentRemoved;
    public Event<ComponentReplaced> OnComponentReplaced;
    public Event<EntityReleased> OnEntityReleased;
    public Event<PoolChanged> OnEntityCreated;
    public Event<PoolChanged> OnEntityWillBeDestroyed;
    public Event<PoolChanged>OnEntityDestroyed;
    public Event<PoolGroupChanged> OnGroupCreated;
    public Event<PoolGroupChanged> OnGroupCleared;

    public EventSource() {
        OnComponentAdded = new Event();
        OnComponentRemoved = new Event();
        OnComponentReplaced = new Event();
        OnEntityReleased = new Event();
        OnEntityCreated = new Event();
        OnEntityWillBeDestroyed = new Event();
        OnEntityDestroyed = new Event();
        OnGroupCreated = new Event();
        OnGroupCleared = new Event();

    }
}

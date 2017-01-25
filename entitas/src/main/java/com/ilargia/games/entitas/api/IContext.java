package com.ilargia.games.entitas.api;


import com.ilargia.games.entitas.api.events.ContextEntityChanged;
import com.ilargia.games.entitas.api.events.ContextGroupChanged;
import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.api.matcher.IMatcher;

import java.util.Stack;

public interface IContext<TEntity extends IEntity> {


    // Eventos
    Event<ContextEntityChanged> OnEntityCreated = new Event<>();
    Event<ContextEntityChanged> OnEntityWillBeDestroyed = new Event<>();
    Event<ContextEntityChanged> OnEntityDestroyed = new Event<>();
    Event<ContextGroupChanged> OnGroupCreated = new Event<>();
    Event<ContextGroupChanged> OnGroupCleared = new Event<>();

    TEntity createEntity();

    void destroyEntity(TEntity entity);

    boolean hasEntity(TEntity entity);

    TEntity[] getEntities();

    IGroup<TEntity> getGroup(IMatcher<TEntity> matcher);

    int getTotalComponents();

    Stack<IComponent>[] getComponentPools();

    ContextInfo getContextInfo();

    int getCount();

    int getReusableEntitiesCount();

    int getRetainedEntitiesCount();

    void destroyAllEntities();

    void clearGroups();

    void addEntityIndex(String name, IEntityIndex entityIndex);

    IEntityIndex getEntityIndex(String name);

    void deactivateAndRemoveEntityIndices();

    void resetCreationIndex();

    void clearComponentPool(int index);

    void clearComponentPools();

    void reset();


    default void notifyEntityCreated(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityCreated.listeners()) {
            listener.changed(context, entity);
        }
    }

    default void notifyEntityWillBeDestroyed(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityWillBeDestroyed.listeners()) {
            listener.changed(context, entity);
        }
    }

    default void notifyEntityDestroyed(IContext context, IEntity entity) {
        for (ContextEntityChanged listener : OnEntityDestroyed.listeners()) {
            listener.changed(context, entity);
        }
    }

    default void notifyGroupCreated(IContext context, IGroup group) {
        for (ContextGroupChanged listener : OnGroupCreated.listeners()) {
            listener.changed(context, group);
        }
    }

    default void notifyGroupCleared(IContext context, IGroup group) {
        for (ContextGroupChanged listener : OnGroupCleared.listeners()) {
            listener.changed(context, group);
        }
    }

    default void clearEventsPool() {
        OnEntityCreated.clear();
        OnEntityWillBeDestroyed.clear();
        OnEntityDestroyed.clear();
        OnGroupCreated.clear();
        OnGroupCleared.clear();
    }

}

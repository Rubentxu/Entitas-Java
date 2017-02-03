package com.ilargia.games.entitas.api;


import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.events.ContextEntityChanged;
import com.ilargia.games.entitas.api.events.ContextGroupChanged;
import com.ilargia.games.entitas.api.events.EntityComponentChanged;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

public interface IContext<TEntity extends IEntity> {

    // Eventos
    Map<IContext, Set<ContextEntityChanged>> OnEntityCreated = Collections.createMap(IContext.class, Set.class);
    Map<IContext, Set<ContextEntityChanged>> OnEntityWillBeDestroyed= Collections.createMap(IContext.class, Set.class);;
    Map<IContext, Set<ContextEntityChanged>> OnEntityDestroyed= Collections.createMap(IContext.class, Set.class);;
    Map<IContext, Set<ContextGroupChanged>> OnGroupCreated= Collections.createMap(IContext.class, Set.class);;
    Map<IContext, Set<ContextGroupChanged>> OnGroupCleared= Collections.createMap(IContext.class, Set.class);;


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

    default void clearEventsListener() {
        if (OnEntityCreated.containsKey(this)) OnEntityCreated.get(this).clear();
        if (OnEntityWillBeDestroyed.containsKey(this)) OnEntityWillBeDestroyed.get(this).clear();
        if (OnEntityDestroyed.containsKey(this)) OnEntityDestroyed.clear();
        if (OnGroupCreated.containsKey(this)) OnGroupCreated.get(this).clear();
        if (OnGroupCleared.containsKey(this)) OnGroupCleared.get(this).clear();

    }

    Collector createCollector(IMatcher matcher);

    Collector createCollector(IMatcher matcher, GroupEvent groupEvent);

    Collector createEntityCollector(Context[] contexts, IMatcher matcher);

    Collector createEntityCollector(Context[] contexts, IMatcher matcher, GroupEvent eventType);


    default void OnEntityCreated(ContextEntityChanged listener ) {
        if (!OnEntityCreated.containsKey(this)) {
            OnEntityCreated.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnEntityCreated.get(this).add(listener);
    }

    default void OnEntityWillBeDestroyed(ContextEntityChanged listener ) {
        if (!OnEntityWillBeDestroyed.containsKey(this)) {
            OnEntityWillBeDestroyed.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnEntityWillBeDestroyed.get(this).add(listener);
    }

    default void OnEntityDestroyed(ContextEntityChanged listener ) {
        if (!OnEntityDestroyed.containsKey(this)) {
            OnEntityDestroyed.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnEntityDestroyed.get(this).add(listener);
    }

    default void OnGroupCreated(ContextGroupChanged listener ) {
        if (!OnGroupCreated.containsKey(this)) {
            OnGroupCreated.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnGroupCreated.get(this).add(listener);
    }

    default void OnGroupCleared(ContextGroupChanged listener ) {
        if (!OnGroupCleared.containsKey(this)) {
            OnGroupCleared.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnGroupCleared.get(this).add(listener);
    }

    default void notifyEntityCreated(IEntity entity) {
        if (OnEntityCreated.containsKey(this)) {
            for (ContextEntityChanged listener : OnEntityCreated.get(this)) {
                listener.changed(this, entity);
            }
        }
    }


    default void notifyEntityWillBeDestroyed(IEntity entity) {
        if (OnEntityWillBeDestroyed.containsKey(this)) {
            for (ContextEntityChanged listener : OnEntityWillBeDestroyed.get(this)) {
                listener.changed(this, entity);
            }
        }
    }


    default void notifyEntityDestroyed(IEntity entity) {
        if (OnEntityDestroyed.containsKey(this)) {
            for (ContextEntityChanged listener : OnEntityDestroyed.get(this)) {
                listener.changed(this, entity);
            }
        }
    }


    default void notifyGroupCreated(IGroup group) {
        if (OnGroupCreated.containsKey(this)) {
            for (ContextGroupChanged listener : OnGroupCreated.get(this)) {
                listener.changed(this, group);
            }
        }
    }


    default void notifyGroupCleared(IGroup group) {
        if (OnGroupCleared.containsKey(this)) {
            for (ContextGroupChanged listener : OnGroupCleared.get(this)) {
                listener.changed(this, group);
            }
        }
    }

}

package com.ilargia.games.entitas.api;


import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.events.ContextEntityChanged;
import com.ilargia.games.entitas.api.events.ContextGroupChanged;
import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;

import java.util.Stack;

public interface IContext<TEntity extends IEntity> {

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

    void notifyEntityCreated(IContext context, IEntity entity);

    void notifyEntityWillBeDestroyed(IContext context, IEntity entity);

    void notifyEntityDestroyed(IContext context, IEntity entity);

    void notifyGroupCreated(IContext context, IGroup group);

    void notifyGroupCleared(IContext context, IGroup group);

    void clearEventsPool();

    Collector createCollector(IMatcher matcher);

    Collector createCollector(IMatcher matcher, GroupEvent groupEvent);

    Collector createEntityCollector(Context[] contexts, IMatcher matcher);

    Collector createEntityCollector(Context[] contexts, IMatcher matcher, GroupEvent eventType);

}

package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.api.events.EventMap;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.events.GroupUpdated;
import com.ilargia.games.entitas.api.matcher.IMatcher;

public interface IGroup<TEntity extends IEntity> {

    int getCount();

    void removeAllEventHandlers();

    IMatcher<TEntity> getMatcher();

    void handleEntitySilently(TEntity entity);

    void handleEntity(TEntity entity, int index, IComponent component);

    Event<GroupChanged> handleEntity(TEntity entity);

    void updateEntity(TEntity entity, int index, IComponent previousComponent, IComponent newComponent);

    boolean containsEntity(TEntity entity);

    TEntity[] getEntities();

    TEntity getSingleEntity();

    void notifyOnEntityAdded(IGroup<TEntity> group, TEntity entity, int index, IComponent component);

    void notifyOnEntityRemoved(IGroup<TEntity> group, TEntity entity, int index, IComponent component);

    void notifyOnEntityUpdated(IGroup<TEntity> group, TEntity entity, int index, IComponent component, IComponent newComponent);

    void clearEventsPool();


}

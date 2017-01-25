package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.api.events.EventMap;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.events.GroupUpdated;
import com.ilargia.games.entitas.api.matcher.IMatcher;

public interface IGroup<TEntity extends IEntity> {

    Event<GroupChanged> OnEntityAdded = new Event();
    Event<GroupChanged> OnEntityRemoved = new Event();
    Event<GroupUpdated> OnEntityUpdated = new Event();


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

    default void notifyOnEntityAdded(IGroup<TEntity> group, TEntity entity, int index, IComponent component) {
        for (GroupChanged<TEntity> listener : OnEntityAdded.listeners()) {
            listener.changed(group, entity, index, component);
        }
    }

    default void notifyOnEntityRemoved(IGroup<TEntity> group, TEntity entity, int index, IComponent component) {
        for (GroupChanged<TEntity> listener : OnEntityRemoved.listeners()) {
            listener.changed(group, entity, index, component);
        }
    }

    default void notifyOnEntityUpdated(IGroup<TEntity> group, TEntity entity, int index, IComponent component, IComponent newComponent) {
        for (GroupUpdated<TEntity> listener : OnEntityUpdated.listeners()) {
            listener.updated(group, entity, index, component, newComponent);
        }
    }


    default void clearEventsPool() {
        OnEntityAdded.clear();
        OnEntityRemoved.clear();
        OnEntityUpdated.clear();

    }


}

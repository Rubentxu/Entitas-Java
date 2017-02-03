package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.events.GroupUpdated;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;
import java.util.Set;

public interface IGroup<TEntity extends IEntity> {

    Map<IGroup, Set<GroupChanged>> OnEntityAdded = Collections.createMap(IGroup.class, Set.class);
    Map<IGroup, Set<GroupChanged>> OnEntityRemoved = Collections.createMap(IGroup.class, Set.class);
    Map<IGroup, Set<GroupUpdated>> OnEntityUpdated = Collections.createMap(IGroup.class, Set.class);

    int getCount();

    void removeAllEventHandlers();

    IMatcher<TEntity> getMatcher();

    void handleEntitySilently(TEntity entity);

    void handleEntity(TEntity entity, int index, IComponent component);

    Set<GroupChanged> handleEntity(TEntity entity);

    void updateEntity(TEntity entity, int index, IComponent previousComponent, IComponent newComponent);

    boolean containsEntity(TEntity entity);

    TEntity[] getEntities();

    TEntity getSingleEntity();


    default void clearEventsListener() {
        if(OnEntityAdded.containsKey(this)) OnEntityAdded.get(this).clear();
        if(OnEntityRemoved.containsKey(this)) OnEntityRemoved.get(this).clear();
        if(OnEntityUpdated.containsKey(this)) OnEntityUpdated.get(this).clear();

    }

    default void OnEntityAdded(GroupChanged<TEntity> listener ) {
        if (!OnEntityAdded.containsKey(this)) {
            OnEntityAdded.put(this, Collections.createSet(GroupChanged.class));
        }
        OnEntityAdded.get(this).add(listener);
    }

    default void OnEntityUpdated(GroupUpdated<TEntity> listener ) {
        if (!OnEntityUpdated.containsKey(this)) {
            OnEntityUpdated.put(this, Collections.createSet(GroupChanged.class));
        }
        OnEntityUpdated.get(this).add(listener);
    }

    default void OnEntityRemoved(GroupChanged<TEntity> listener ) {
        if (!OnEntityRemoved.containsKey(this)) {
            OnEntityRemoved.put(this, Collections.createSet(GroupChanged.class));
        }
        OnEntityRemoved.get(this).add(listener);
    }


    default void notifyOnEntityAdded(TEntity entity, int index, IComponent component) {
        if (OnEntityAdded.containsKey(this)) {
            for (GroupChanged<TEntity> listener : OnEntityAdded.get(this)) {
                listener.changed(this, entity, index, component);
            }
        }
    }

    default void notifyOnEntityUpdated(TEntity entity, int index, IComponent component, IComponent newComponent) {
        if (OnEntityUpdated.containsKey(this)) {
            for (GroupUpdated<TEntity> listener : OnEntityUpdated.get(this)) {
                listener.updated(this, entity, index, component, newComponent);
            }
        }
    }

    default void notifyOnEntityRemoved( TEntity entity, int index, IComponent component) {
        if (OnEntityRemoved.containsKey(this)) {
            for (GroupChanged<TEntity> listener : OnEntityRemoved.get(this)) {
                listener.changed(this, entity, index, component);
            }
        }
    }


}

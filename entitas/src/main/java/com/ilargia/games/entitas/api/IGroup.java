package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.entitas.IEntity;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.matcher.IMatcher;

import java.util.Set;

public interface IGroup<TEntity extends IEntity> {

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


}

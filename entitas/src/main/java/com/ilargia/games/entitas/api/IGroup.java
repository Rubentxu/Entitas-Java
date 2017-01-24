package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.api.matcher.IMatcher;

public interface IGroup<TEntity extends IEntity> {

    int getCount();

    void removeAllEventHandlers();

    IMatcher<TEntity> getMatcher();

    void handleEntitySilently(TEntity entity);

    void handleEntity(TEntity entity, int index, IComponent component);

    GroupChanged<TEntity> handleEntity(TEntity entity);

    void updateEntity(TEntity entity, int index, IComponent previousComponent, IComponent newComponent);

    boolean containsEntity(TEntity entity);

    TEntity[] getEntities();

    TEntity getSingleEntity();

}

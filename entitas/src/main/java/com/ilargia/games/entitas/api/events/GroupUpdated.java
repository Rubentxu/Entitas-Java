package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.entitas.IEntity;
import com.ilargia.games.entitas.api.IGroup;

@FunctionalInterface
public interface GroupUpdated<TEntity extends IEntity> {
    void updated(final IGroup<TEntity> group, final TEntity entity, final int index, final IComponent previousComponent, final IComponent newComponent);

}
package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface EntityComponentReplaced<TEntity extends IEntity> {
    void replaced(final TEntity entity, final int index, final IComponent previousComponent, final IComponent newComponent);

}

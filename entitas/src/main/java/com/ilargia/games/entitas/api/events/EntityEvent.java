package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.entitas.IEntity;

@FunctionalInterface
public interface EntityEvent<TEntity extends IEntity> {
    void released(final TEntity entity);
}

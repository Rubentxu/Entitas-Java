package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface EntityReleased<TEntity extends IEntity> {
    void released(final TEntity entity);
}

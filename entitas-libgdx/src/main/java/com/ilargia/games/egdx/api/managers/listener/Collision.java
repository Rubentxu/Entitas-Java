package com.ilargia.games.egdx.api.managers.listener;

import com.ilargia.games.entitas.api.IEntity;

public interface Collision<TEntity extends IEntity> {
    void processCollision(TEntity entityA, TEntity entityB, boolean collisionSignal);

}

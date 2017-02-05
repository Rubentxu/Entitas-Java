package com.ilargia.games.egdx.base.interfaces;

import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface GameElementFactory<E extends IEntity> {
    E generate(IEntity entity);
}
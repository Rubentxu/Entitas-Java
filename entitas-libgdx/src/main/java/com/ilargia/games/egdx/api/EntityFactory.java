package com.ilargia.games.egdx.api;

import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface EntityFactory<E extends IEntity> {

    default void loadAssets(Engine engine) {
    }

    E create(float posX, float posY);
}
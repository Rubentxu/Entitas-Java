package com.ilargia.games.egdx.api;

import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IEntity;

public interface EntityFactory<E extends IEntity> {

    void loadAssets(Engine engine);

    E create(Engine engine, IContext<E> context, float posX, float posY);
}
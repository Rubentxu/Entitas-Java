package com.ilargia.games.egdx.api;

import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.Entity;

public interface EntityFactory<E extends Entity> {

    void loadAssets(Engine engine);

    E create(Engine engine, Context<E> context);
}
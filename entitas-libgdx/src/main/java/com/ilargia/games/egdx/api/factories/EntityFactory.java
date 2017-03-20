package com.ilargia.games.egdx.api.factories;

import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IContexts;

public interface EntityFactory<C extends IContexts,E extends Entity> {

    void loadAssets(Engine engine);

    E create(Engine engine, C context);
}
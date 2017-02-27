package com.ilargia.games.egdx.api;

import com.ilargia.games.entitas.api.IEntity;

public interface GUIFactory<E extends IEntity> {

    void loadAssets(Engine engine);

    E create(Engine engine);
}
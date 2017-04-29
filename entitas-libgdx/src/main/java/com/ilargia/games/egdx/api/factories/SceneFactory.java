package com.ilargia.games.egdx.api.factories;

import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.entitas.api.IContexts;

public interface  SceneFactory<E extends Engine, C extends IContexts> {
    public void createScene(E engine, C entitas);
}

package com.ilargia.games.egdx.api.factories;

import com.ilargia.games.egdx.api.managers.SceneManager;
import com.ilargia.games.entitas.api.IComponent;

@FunctionalInterface
public interface LightFactory<S extends SceneManager, C extends IComponent, L> {
     L createLigth(S sceneManager, C componet);
}

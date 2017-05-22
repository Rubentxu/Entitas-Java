package ilargia.egdx.api.factories;

import ilargia.egdx.api.managers.SceneManager;
import ilargia.entitas.api.IComponent;

@FunctionalInterface
public interface LightFactory<S extends SceneManager, C extends IComponent, L> {
     L createLigth(S sceneManager, C componet);
}

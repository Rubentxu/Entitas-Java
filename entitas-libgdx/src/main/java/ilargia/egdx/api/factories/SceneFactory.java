package ilargia.egdx.api.factories;

import ilargia.egdx.api.Engine;
import ilargia.entitas.api.IContexts;

public interface  SceneFactory<E extends Engine, C extends IContexts> {
    public void createScene(E engine, C entitas);
}

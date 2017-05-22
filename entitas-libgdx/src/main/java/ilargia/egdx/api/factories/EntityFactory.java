package ilargia.egdx.api.factories;

import ilargia.egdx.api.Engine;
import ilargia.entitas.Entity;
import ilargia.entitas.api.IContexts;

public interface EntityFactory<C extends IContexts,E extends Entity> {

    void loadAssets(Engine engine);

    E create(Engine engine, C context);
}
package ilargia.egdx.api;

import ilargia.egdx.api.managers.InputManager;
import ilargia.entitas.api.IContexts;

@FunctionalInterface
public interface GameController <I extends InputManager, C extends IContexts> {
    void update(I inputManager, C context);
}

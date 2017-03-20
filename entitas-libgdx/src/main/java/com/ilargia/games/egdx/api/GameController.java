package com.ilargia.games.egdx.api;

import com.ilargia.games.egdx.api.managers.InputManager;
import com.ilargia.games.entitas.api.IContexts;

@FunctionalInterface
public interface GameController <I extends InputManager, C extends IContexts> {
    void update(I inputManager, C context);
}

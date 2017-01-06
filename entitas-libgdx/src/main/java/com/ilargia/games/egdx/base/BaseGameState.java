package com.ilargia.games.egdx.base;

import com.ilargia.games.egdx.base.interfaces.GameState;
import com.ilargia.games.entitas.Systems;

public abstract class BaseGameState implements GameState {
    protected final Systems systems;

    public BaseGameState() {
        this.systems = new Systems();
    }

    @Override
    public void init() {
        systems.initialize();
    }

    @Override
    public void update(float deltaTime) {
        systems.execute(deltaTime);
    }

    @Override
    public void render() {
        systems.render();
        systems.cleanup();
    }

}

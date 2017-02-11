package com.ilargia.games.egdx.api.base;

import com.ilargia.games.egdx.api.GameState;
import com.ilargia.games.entitas.systems.Systems;

public abstract class BaseGameState implements GameState {
    protected final Systems systems;

    public BaseGameState() {
        this.systems = new Systems();
    }

    @Override
    public void init() {
        initialize();
        systems.initialize();
    }

    public abstract void initialize();

    @Override
    public void update(float deltaTime) {
        systems.execute(deltaTime);
    }

    @Override
    public void render() {
        systems.render();
        systems.cleanup();
    }

    @Override
    public void dispose() {
        systems.tearDown();
    }

}

package ilargia.egdx.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import ilargia.egdx.api.GameState;
import ilargia.entitas.systems.Systems;

public abstract class GameStateGDX implements GameState {
    protected final Systems systems;

    public GameStateGDX() {
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
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        systems.cleanup();
        systems.render();

    }

    @Override
    public void dispose() {
        systems.tearDown();
    }

}

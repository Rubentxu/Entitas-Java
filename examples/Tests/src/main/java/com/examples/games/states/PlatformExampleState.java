package com.examples.games.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.examples.games.ExamplesEngine;
import com.examples.games.entities.Ground;
import com.examples.games.entities.Mariano;
import com.ilargia.games.egdx.api.managers.SceneManager;
import com.ilargia.games.egdx.impl.GameStateGDX;
import com.ilargia.games.egdx.impl.managers.GUIManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;


public class PlatformExampleState extends GameStateGDX {
    private final ExamplesEngine engine;

    public PlatformExampleState(ExamplesEngine engine) {
        this.engine = engine;

    }

    @Override
    public void loadResources() {
        SceneManagerGDX sceneManager = engine.getManager(SceneManagerGDX.class);
        sceneManager.addEntityFactory("Ground", new Ground());
        //sceneManager.addEntityFactory("Mariano", new Mariano());
    }

    @Override
    public void initialize() {
        systems.add(new InputSystem(context.core))
                .add(new ContactSystem(context.core))
                .add(new BoundsSystem(context.core))
                .add(new MoveSystem(context.core))
                .add(new RendererSystem(context.core, engine.sr, camera, batch, font));
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {
        //context.core.destroyAllEntities();
        systems.clearSystems();
    }

}

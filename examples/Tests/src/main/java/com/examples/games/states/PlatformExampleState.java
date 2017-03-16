package com.examples.games.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.examples.games.ExamplesEngine;
import com.ilargia.games.egdx.impl.GameStateGDX;
import com.ilargia.games.egdx.impl.managers.GUIManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;


public class PlatformExampleState extends GameStateGDX {

    private final ExamplesEngine engine;
    private final Entitas entitas;

    public PlatformExampleState(ExamplesEngine engine) {
        this.engine = engine;
        this.entitas = new Entitas();
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void initialize() {
        // Input
        Camera camera = engine.getManager(SceneManagerGDX.class).getDefaultCamera();
        Batch batch = engine.getManager(SceneManagerGDX.class).getBatch();

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

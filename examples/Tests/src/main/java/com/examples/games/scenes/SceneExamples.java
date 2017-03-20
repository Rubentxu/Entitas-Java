package com.examples.games.scenes;

import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.factories.SceneFactory;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;


public class SceneExamples implements SceneFactory<Engine, Entitas> {

    @Override
    public void createScene(Engine engine, Entitas entitas) {
        SceneManagerGDX sceneManager = engine.getManager(SceneManagerGDX.class);
        GameEntity ground = sceneManager.createEntity("Ground");
        ground.getRigidBody().body.setTransform(10,1,0);

        GameEntity mariano = sceneManager.createEntity("Mariano");
        mariano.getRigidBody().body.setTransform(10,6,0);
    }
}

package com.examples.games.scenes;

import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;


public class SceneManagerExamples extends SceneManagerGDX {

    public SceneManagerExamples(EngineGDX engine, Entitas entitas) {
        super(engine, entitas);
    }

    @Override
    public void createScene(String scene) {
        initialize();
        GameEntity ground = createEntity("Ground");
        ground.getRigidBody().body.setTransform(10,1,0);

        GameEntity mariano = createEntity("Mariano");
        mariano.getRigidBody().body.setTransform(10,6,0);


    }
}

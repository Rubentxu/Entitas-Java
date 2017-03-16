package com.examples.games.entities;

import com.examples.games.ExamplesEngine;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.util.BodyBuilder;

public class Ground implements EntityFactory<GameEntity>{
    @Override
    public void loadAssets(Engine engine) {

    }

    @Override
    public GameEntity create(Engine engine) {
        BodyBuilder bodyBuilder = engine.getManager(PhysicsManagerGDX.class).getBodyBuilder();
        Entitas entitas = ((ExamplesEngine) engine).entitas;

        return entitas.game.createEntity().addIdentity("Ground", "Ground")
                .addRigidBody(bodyBuilder.fixture(bodyBuilder.fixtureDefBuilder.boxShape(50, 0.5f)
                .friction(0.5f))
                .build());


    }

}

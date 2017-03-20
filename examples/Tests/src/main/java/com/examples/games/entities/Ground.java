package com.examples.games.entities;

import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.factories.EntityFactory;
import com.ilargia.games.egdx.impl.managers.AssetsManagerGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.util.BodyBuilder;


public class Ground implements EntityFactory<Entitas, GameEntity> {

    private AssetsManagerGDX assetsManager;

    @Override
    public void loadAssets(Engine engine) {
        assetsManager = engine.getManager(AssetsManagerGDX.class);
        assetsManager.loadTexture("assets/textures/pong.jpg");
    }

    @Override
    public GameEntity create(Engine engine, Entitas entitas) {
        BodyBuilder bodyBuilder = engine.getManager(PhysicsManagerGDX.class).getBodyBuilder();

        return entitas.game.createEntity().addTags("Ground")
                .addRigidBody(bodyBuilder.fixture(bodyBuilder.fixtureDefBuilder
                        .boxShape(50, 0.5f)
                        .friction(0.5f))
                        .build());
                //.addTextureView(new TextureRegion(assetsManager.getTexture("assets/textures/pong.jpg")),
                 //       new Bounds(30, 30), false, false, 1, 1, Color.WHITE);

    }

}
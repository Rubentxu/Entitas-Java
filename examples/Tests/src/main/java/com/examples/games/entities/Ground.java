package com.examples.games.entities;

import ilargia.egdx.api.Engine;
import ilargia.egdx.api.factories.EntityFactory;
import ilargia.egdx.impl.managers.AssetsManagerGDX;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.util.BodyBuilder;


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
                .setInteractive(true)
                .addRigidBody(bodyBuilder.fixture(bodyBuilder.fixtureDefBuilder
                        .boxShape(150, 0.5f)
                        .friction(0.5f))
                        .build());
                //.addTextureView(new TextureRegion(assetsManager.getTexture("assets/textures/pong.jpg")),
                 //       new Bounds(30, 30), false, false, 1, 1, Color.WHITE);

    }

}

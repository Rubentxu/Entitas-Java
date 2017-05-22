package com.examples.games.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import ilargia.egdx.api.Engine;
import ilargia.egdx.api.factories.EntityFactory;
import ilargia.egdx.impl.managers.AssetsManagerGDX;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.data.Bounds;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.util.BodyBuilder;


public class Box implements EntityFactory<Entitas, GameEntity> {
    private String box2 = "assets/textures/box2.png";
    private AssetsManagerGDX assetsManager;

    @Override
    public void loadAssets(Engine engine) {
        assetsManager = engine.getManager(AssetsManagerGDX.class);
        assetsManager.loadTexture(box2);
    }

    @Override
    public GameEntity create(Engine engine, Entitas entitas) {
        BodyBuilder bodyBuilder = engine.getManager(PhysicsManagerGDX.class).getBodyBuilder();

        float width = MathUtils.random(0.4f, 1);
        float height =  MathUtils.random(0.4f, 1f);


        GameEntity entity = entitas.game.createEntity()
                .setInteractive(true)
                .addRigidBody(bodyBuilder.fixture(bodyBuilder.fixtureDefBuilder
                        .boxShape(width, height)
                        .friction(0.5f)
                        .density(1f))
                        .type(BodyDef.BodyType.DynamicBody)
                        .build())
                .addTextureView(new TextureRegion(assetsManager.getTexture(box2)), new Bounds(width, height),
                        false, false, 1, 0, Color.WHITE);

        entitas.actuator.createEntity()
                .addRadialGravityActuator(9, 5, 2)
                .addLink(entity.getCreationIndex(), "RadialGravityActuator", true);

        return entity;

    }

}

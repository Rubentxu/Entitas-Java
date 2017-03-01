package com.ilargia.games.states.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.egdx.base.managers.BasePhysicsManager;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.egdx.util.FixtureDefBuilder;
import com.ilargia.games.entitas.Context;
import com.indignado.games.states.game.gen.GameEntity;

public class Block implements EntityFactory<GameEntity> {

    @Override
    public void loadAssets(Engine engine) {

    }

    @Override
    public GameEntity create(Engine engine, Context<GameEntity> context) {

        BasePhysicsManager physics = engine.getManager(BasePhysicsManager.class);
        BodyBuilder bodyBuilder = physics.getBodyBuilder();

        Body bodyBlock = bodyBuilder.fixture(new FixtureDefBuilder()
                .boxShape(1, 1)
                .friction(0.5f)
                .restitution(0.5f))
                .type(BodyDef.BodyType.StaticBody)
                .build();


        GameEntity entity =  context.createEntity()
                .addRigidBody(bodyBlock)
                .addElement("Platform", "Block");


        return entity;
    }
}

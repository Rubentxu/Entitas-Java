package games.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ilargia.games.entitas.egdx.api.Engine;
import com.ilargia.games.entitas.egdx.api.EntityFactory;
import com.ilargia.games.entitas.egdx.base.managers.BasePhysicsManager;
import ilargia.egdx.util.BodyBuilder;
import ilargia.egdx.util.FixtureDefBuilder;
import ilargia.entitas.Context;
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

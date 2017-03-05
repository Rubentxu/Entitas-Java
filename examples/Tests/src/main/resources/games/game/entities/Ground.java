package games.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.entitas.egdx.api.Engine;
import com.ilargia.games.entitas.egdx.api.EntityFactory;
import com.ilargia.games.entitas.egdx.base.managers.BasePhysicsManager;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.egdx.util.FixtureDefBuilder;
import com.ilargia.games.entitas.Context;
import com.indignado.games.states.game.gen.GameContext;
import com.indignado.games.states.game.gen.GameEntity;

public class Ground implements EntityFactory<GameEntity> {

    @Override
    public void loadAssets(Engine engine) {

    }

    @Override
    public GameEntity create(Engine engine, Context<GameEntity> context) {

        BasePhysicsManager physics = engine.getManager(BasePhysicsManager.class);
        BodyBuilder bodyBuilder = physics.getBodyBuilder();

        Body bodyGround = bodyBuilder.fixture(new FixtureDefBuilder()
                .boxShape(50, 0.5f)
                .friction(0.5f))
                .build();


        GameEntity entity = ((GameContext) context).createEntity()
                .addRigidBody(bodyGround)
                .addElement("Platform", "Ground");


        return entity;
    }
}

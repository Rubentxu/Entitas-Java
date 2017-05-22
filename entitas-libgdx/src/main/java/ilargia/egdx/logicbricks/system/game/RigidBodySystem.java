package ilargia.egdx.logicbricks.system.game;


import com.badlogic.gdx.physics.box2d.Body;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.game.GameMatcher;
import ilargia.entitas.api.IContext;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.systems.ReactiveSystem;

import java.util.List;

public class RigidBodySystem extends ReactiveSystem<GameEntity> {

    public RigidBodySystem(Entitas entitas) {
        super(entitas.game);
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.RigidBody());
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            Body body = e.getRigidBody().body;
            body.setUserData(e.getCreationIndex());
        }

    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasRigidBody();
    }
}

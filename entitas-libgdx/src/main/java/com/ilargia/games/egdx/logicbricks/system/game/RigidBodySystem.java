package com.ilargia.games.egdx.logicbricks.system.game;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;

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

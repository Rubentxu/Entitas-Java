package com.ilargia.games.egdx.logicbricks.system.game;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class DestroySystem extends ReactiveSystem<GameEntity> {
    private GameContext context;
    private World physics;

    public DestroySystem(GameContext context, World world) {
        super(context);
        this.physics = world;
        this.context = context;
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.Destroy());
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            if (e.hasRigidBody()) {
                Body body = e.getRigidBody().body;
                body.setUserData(null);
                e.release(body);
                physics.destroyBody(body);
            }
            context.destroyEntity(e);

        }
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.isDestroy();
    }

}

package com.indignado.games.states.game.system;


import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.indignado.games.states.game.gen.game.GameContext;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.game.GameMatcher;
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
            if (e.hasRigidBody()) physics.destroyBody(e.getRigidBody().body);
            context.destroyEntity(e);

        }
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.isDestroy();
    }
}

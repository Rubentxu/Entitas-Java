package com.ilargia.games.entitas.systems;

import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;

import java.util.List;


public class DestroySystem extends ReactiveSystem<GameEntity> {
    private GameContext context;

    public DestroySystem(GameContext context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.Destroy());
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            context.destroyEntity(e);
        }
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.isDestroy();
    }
}

package com.ilargia.games.egdx.logicbricks.system.game;


import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.GameIndex;
import com.ilargia.games.egdx.logicbricks.index.SensorIndex;
import com.ilargia.games.egdx.logicbricks.index.SimpleGameIndex;

import java.util.List;

public class IndexingEntitiesSystem extends ReactiveSystem<GameEntity> {
    private final GameContext gameContext;

    public IndexingEntitiesSystem(Entitas entitas) {
        super(entitas.game);
        this.gameContext = entitas.game;
        SimpleGameIndex.createGameEntityIndices(entitas.game);

    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.Identity());
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasIdentity();
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            SimpleGameIndex.addGameEntity(gameContext, e);
        }

    }


}

package com.ilargia.games.logicbrick.system.game;


import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;
import com.ilargia.games.logicbrick.index.GameIndex;
import com.ilargia.games.logicbrick.index.SensorIndex;
import com.ilargia.games.logicbrick.index.SimpleGameIndex;

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
            SimpleGameIndex.addGameEntity(gameContext, e.getCreationIndex(), e);
        }

    }


}

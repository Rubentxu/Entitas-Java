package com.ilargia.games.systems;


import com.ilargia.games.core.Entitas;
import com.ilargia.games.core.gen.game.GameEntity;
import com.ilargia.games.core.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.group.GroupEvent;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class ScoreSystem extends ReactiveSystem<GameEntity> implements IInitializeSystem {

    private Entitas entitas;

    public ScoreSystem(Entitas entitas) {
        super(entitas.game);
        this.entitas = entitas;
    }

    @Override
    public void initialize() {
        entitas.gamestate.setScore(0);
    }

    @Override
    protected Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return context.createCollector(GameMatcher.GameBoardElement(), GroupEvent.Removed);
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return true;
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        entitas.gamestate.replaceScore(entitas.gamestate.getScore().value + entities.size());
    }
}

package com.ilargia.games.systems;

import com.ilargia.games.core.Entitas;
import com.ilargia.games.core.GameMatcher;
import com.ilargia.games.core.GameSessionEntity;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class ScoreSystem extends ReactiveSystem<GameSessionEntity> implements IInitializeSystem {

    private Entitas entitas;

    public ScoreSystem(Entitas entitas) {
        super(entitas.gamesession);
        this.entitas = entitas;
    }

    @Override
    public void initialize() {
        entitas.gamesession.setScore(0);
    }

    @Override
    protected Collector<GameSessionEntity> getTrigger(IContext<GameSessionEntity> context) {
        return context.createCollector(GameMatcher.GameBoardElement(), GroupEvent.Removed);
    }

    @Override
    protected boolean filter(GameSessionEntity entity) {
        return true;
    }

    @Override
    protected void execute(List<GameSessionEntity> entities) {
        entitas.gamesession.replaceScore(entitas.gamesession.getScore().value + entities.size());
    }
}

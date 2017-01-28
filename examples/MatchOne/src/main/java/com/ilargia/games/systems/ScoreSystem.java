package com.ilargia.games.systems;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class ScoreSystem implements ISetPools<Pools>, IReactiveSystem<Entity>, IInitializeSystem {

    private Pools _pools;

    @Override
    public void setPools(Pools pools) {
       _pools =  pools;
    }

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.GameBoardElement().OnEntityRemoved();
    }

    @Override
    public void initialize() {
        _pools.score.setScore(0);
    }

    @Override
    public void execute(Array<Entity> entities) {
        _pools.score.replaceScore(_pools.score.getScore().value + entities.size);

    }


}

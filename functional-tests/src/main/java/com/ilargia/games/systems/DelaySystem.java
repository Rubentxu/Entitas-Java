package com.ilargia.games.systems;

import com.ilargia.games.components.Delay;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.egdx.EGGame;
import com.ilargia.games.egdx.events.game.GameEvent;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;


public class DelaySystem implements IExecuteSystem, ISetPool<Pool> {

    private Group<Entity> _group;


    @Override
    public void setPool(Pool pool) {
        _group = pool.getGroup(CoreMatcher.Delay());

    }

    @Override
    public void execute(float deltatime) {

        for (Entity e : _group.getEntities()) {
            Delay delay = e.getDelay();
            delay.time += deltatime;
            if (delay.time > delay.duration) {
                EGGame.ebus.post(GameEvent.NEXT_STATE);
                delay.time = 0;
            }

        }

    }


}



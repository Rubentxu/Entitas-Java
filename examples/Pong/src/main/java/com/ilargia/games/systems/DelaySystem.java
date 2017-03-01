package com.ilargia.games.systems;

import com.ilargia.games.PongGame;
import com.ilargia.games.core.component.Delay;
import com.ilargia.games.core.CoreContext;
import com.ilargia.games.core.CoreEntity;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.egdx.api.ChangeStateCommand;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;


public class DelaySystem implements IExecuteSystem {

    private Group<CoreEntity> _group;

    public DelaySystem(CoreContext context) {
        _group = context.getGroup(CoreMatcher.Delay());

    }

    @Override
    public void execute(float deltatime) {

        for (CoreEntity e : _group.getEntities()) {
            Delay delay = e.getDelay();
            delay.time += deltatime;
            if (delay.time > delay.duration) {
                PongGame.ebus.post((ChangeStateCommand<PongGame>) (nameState, game) ->
                        game.changeState(game.getPongState(), game.getSlideTransition())
                );
                delay.time = 0;
            }

        }

    }


}



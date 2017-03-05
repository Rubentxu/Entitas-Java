package com.ilargia.games.entitas.systems;

import com.ilargia.games.entitas.core.CoreContext;
import com.ilargia.games.entitas.core.CoreEntity;
import com.ilargia.games.entitas.core.CoreMatcher;
import com.ilargia.games.entitas.egdx.api.ChangeStateCommand;
import com.ilargia.games.entitas.PongGame;
import com.ilargia.games.logicbrick.component.Delay;
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



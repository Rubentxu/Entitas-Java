package com.ilargia.games.systems;

import com.ilargia.games.PongGame;
import com.ilargia.games.components.Delay;
import com.ilargia.games.egdx.base.interfaces.commands.ChangeStateCommand;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.api.system.IExecuteSystem;


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



package com.ilargia.games;

import com.ilargia.games.egdx.base.BaseGame;
import com.ilargia.games.egdx.events.game.GameEvent;
import com.ilargia.games.egdx.base.interfaces.EventBus;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.states.PongState;
import net.engio.mbassy.listener.Handler;


public class PongGame extends BaseGame {


    public PongGame(PongEngine engine, Systems systems, EventBus bus) {
        super(engine, systems, bus);
        ebus.subscribe(this);
    }


    @Handler
    public void handleNextState(GameEvent gmEvent) {
        if (gmEvent.equals(GameEvent.NEXT_STATE)) {
            changeState(new PongState(_systems));
        }
    }

    @Override
    public void init() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getErrorState() {
        return 0;
    }
}

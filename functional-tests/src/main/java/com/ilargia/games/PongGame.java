package com.ilargia.games;

import com.ilargia.games.egdx.EGGame;
import com.ilargia.games.egdx.events.game.GameEvent;
import com.ilargia.games.states.PongState;
import net.engio.mbassy.listener.Handler;


public class PongGame extends EGGame {


    public PongGame(PongEngine engine) {
        super(engine);
        ebus.subscribe (this);
    }


    @Handler
    public void handleNextState(GameEvent gmEvent) {
        if(gmEvent.equals(GameEvent.NEXT_STATE)) {
            changeState(new PongState());
        }
    }
}

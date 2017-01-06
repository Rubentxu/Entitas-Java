package com.ilargia.games;

import com.ilargia.games.egdx.base.BaseGame;
import com.ilargia.games.egdx.events.game.GameEvent;
import com.ilargia.games.egdx.base.interfaces.EventBus;
import com.ilargia.games.egdx.transitions.FadeTransition;
import com.ilargia.games.egdx.transitions.SliceTransition;
import com.ilargia.games.egdx.transitions.SlideTransition;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.states.PongState;
import net.engio.mbassy.listener.Handler;
import com.badlogic.gdx.math.Interpolation;

public class PongGame extends BaseGame<PongEngine> {


    public PongGame(PongEngine engine, EventBus bus) {
        super(engine, bus);
        ebus.subscribe(this);
    }


    @Handler
    public void handleNextState(GameEvent gmEvent) {
        if (gmEvent.equals(GameEvent.NEXT_STATE)) {
           // changeState(new PongState(_engine), new SliceTransition(2,3, 10, Interpolation.bounceIn, _engine));
            changeState(new PongState(_engine), new SlideTransition(1,SlideTransition.DOWN, false, Interpolation.bounceOut, _engine));
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

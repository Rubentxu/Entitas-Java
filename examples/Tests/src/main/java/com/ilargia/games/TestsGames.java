package com.ilargia.games;

import com.badlogic.gdx.math.Interpolation;
import com.ilargia.games.egdx.api.ChangeStateCommand;
import com.ilargia.games.egdx.api.EventBus;
import com.ilargia.games.egdx.base.BaseGame;
import com.ilargia.games.egdx.base.managers.BaseSceneManager;
import com.ilargia.games.egdx.transitions.SlideTransition;
import com.ilargia.games.states.PongState;
import net.engio.mbassy.listener.Handler;

public class PongGame extends BaseGame<PongEngine> {


    private SlideTransition slideTransition;
    private PongState pongState;

    public PongGame(PongEngine engine, EventBus bus) {
        super(engine, bus);
        ebus.subscribe(this);
    }


    @Handler
    public void handleChangeState(ChangeStateCommand command) {
        command.change("GameState", this);
    }

    @Override
    public void init() {

    }

    public SlideTransition getSlideTransition() {
        if (slideTransition == null)
            slideTransition = new SlideTransition(1, SlideTransition.DOWN, false, Interpolation.bounceOut, _engine.getManager(BaseSceneManager.class).getBatch());
        return slideTransition;
    }


    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public int getErrorState() {
        return 0;
    }

    public PongState getPongState() {
        if (pongState == null)
            pongState = new PongState(_engine);
        return pongState;
    }
}

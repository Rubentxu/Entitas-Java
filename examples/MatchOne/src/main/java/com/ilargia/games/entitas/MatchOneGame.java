package com.ilargia.games.entitas;

import com.badlogic.gdx.math.Interpolation;

import com.ilargia.games.entitas.egdx.api.ChangeStateCommand;
import com.ilargia.games.entitas.egdx.api.EventBus;
import com.ilargia.games.entitas.egdx.api.GameState;
import com.ilargia.games.entitas.egdx.base.BaseGame;
import com.ilargia.games.entitas.egdx.base.managers.BaseSceneManager;
import com.ilargia.games.entitas.egdx.transitions.SlideTransition;
import com.indignado.games.states.MatchOneState;
import net.engio.mbassy.listener.Handler;

public class MatchOneGame extends BaseGame<MatchOneEngine> {


    private SlideTransition slideTransition;
    private MatchOneState pongState;

    public MatchOneGame(MatchOneEngine engine, EventBus bus) {
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
            slideTransition = new SlideTransition(1, SlideTransition.DOWN, false, Interpolation.bounceOut,
                    _engine.getManager(BaseSceneManager.class).getBatch());
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

    public GameState getPongState() {
        if (pongState == null)
            pongState = new MatchOneState(_engine);
        return pongState;
    }
}

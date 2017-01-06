package com.ilargia.games.egdx.base;

import com.ilargia.games.egdx.base.interfaces.Game;
import com.ilargia.games.egdx.base.interfaces.GameState;
import com.ilargia.games.egdx.base.interfaces.StateTransition;

public abstract class BaseTransition implements StateTransition {

    protected float duration;
    protected float timeTransition;
    protected float alpha;
    protected Game game;
    protected GameState newState;
    protected GameState oldState;

    public BaseTransition(float duration) {
        this.duration = duration;
        this.timeTransition = 0;

    }

    @Override
    public void update(float deltaTime) {
        if (timeTransition >= duration) {
            game.popState();
        } else {
            alpha = timeTransition / duration;
        }
        timeTransition+= deltaTime;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void states(Game game, GameState oldState, GameState newState) {
        this.game = game;
        this.oldState = oldState;
        this.newState = newState;
        game.pushState(newState);
        game.pushState(this);

    }

}

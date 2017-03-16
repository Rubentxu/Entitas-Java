package com.ilargia.games.egdx.impl;

import com.ilargia.games.egdx.api.Game;
import com.ilargia.games.egdx.api.GameState;
import com.ilargia.games.egdx.api.StateTransition;

public abstract class TransitionGDX implements StateTransition {

    protected float duration;
    protected float timeTransition;
    protected float alpha;
    protected Game game;
    protected GameState newState;
    protected GameState oldState;

    public TransitionGDX(float duration) {
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
        timeTransition += deltaTime;
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void states(Game game, GameState oldState, GameState newState) {
        timeTransition = 0;
        this.game = game;
        this.oldState = oldState;
        this.newState = newState;
        game.pushState(newState);
        game.pushState(this);

    }

}

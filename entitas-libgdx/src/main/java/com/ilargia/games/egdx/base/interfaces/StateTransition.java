package com.ilargia.games.egdx.base.interfaces;

public interface StateTransition extends GameState {

    public float getDuration();

    public void states(Game game, GameState oldState, GameState newState);
}
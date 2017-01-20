package com.ilargia.games.egdx.base.interfaces;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface StateTransition extends GameState{

    public float getDuration();

    public void states(Game game, GameState oldState, GameState newState);
}
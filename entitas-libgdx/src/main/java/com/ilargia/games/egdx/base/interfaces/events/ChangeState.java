package com.ilargia.games.egdx.base.interfaces.events;

import com.ilargia.games.egdx.base.interfaces.GameState;

@FunctionalInterface
public interface ChangeState extends GameEvent {
    public void change(final GameState newState, final GameState oldState);
}

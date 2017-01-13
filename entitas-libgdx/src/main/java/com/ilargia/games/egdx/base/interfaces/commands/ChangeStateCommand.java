package com.ilargia.games.egdx.base.interfaces.commands;

import com.ilargia.games.egdx.base.interfaces.Engine;
import com.ilargia.games.egdx.base.interfaces.Game;
import com.ilargia.games.egdx.base.interfaces.events.GameEvent;

@FunctionalInterface
public interface ChangeStateCommand<E extends Engine> extends GameEvent {
    public void change(final String nameState, Game<E> game);
}

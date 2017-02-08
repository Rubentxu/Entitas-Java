package com.ilargia.games.egdx.base.interfaces.commands;

import com.ilargia.games.egdx.base.interfaces.Game;
import com.ilargia.games.egdx.base.interfaces.events.GameEvent;

@FunctionalInterface
public interface ChangeStateCommand<G extends Game> extends GameEvent {
    public void change(final String nameState, G game);
}

package com.ilargia.games.egdx.api;

@FunctionalInterface
public interface ChangeStateCommand<G extends Game> extends GameEvent {
    public void change(final String nameState, G game);
}
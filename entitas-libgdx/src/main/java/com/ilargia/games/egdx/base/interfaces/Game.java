package com.ilargia.games.egdx.base.interfaces;


public interface Game<E extends Engine> {

    public void init();

    public void update(float deltaTime);

    public void dispose();

    public void pushState(GameState<E> state);

    public GameState<E> popState();

    public GameState<E> changeState(GameState<E> state);

    public boolean isRunning();

    public int getErrorState();

    public void clear();

}

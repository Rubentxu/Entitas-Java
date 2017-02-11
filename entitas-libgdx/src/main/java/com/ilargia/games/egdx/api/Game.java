package com.ilargia.games.egdx.api;


public interface Game<E> {

    public void init();

    public void update(float deltaTime);

    public void dispose();

    public void pushState(GameState state);

    public GameState popState();

    public GameState changeState(GameState state);

    public void changeState(GameState state, StateTransition transition);

    public boolean isRunning();

    public int getErrorState();

    public void clear();

    public E getEngine();

}

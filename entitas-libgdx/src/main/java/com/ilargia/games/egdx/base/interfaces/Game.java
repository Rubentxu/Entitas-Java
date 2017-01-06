package com.ilargia.games.egdx.base.interfaces;


public interface Game {

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

}

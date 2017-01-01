package com.ilargia.games.egdx.interfaces;


public interface Game {

    public void init(String [] args);

    public void update(float deltaTime);

    public void dispose();

    public void pushState(GameState state);

    public GameState popState();

    public GameState changeState(GameState state);

    public boolean isRunning();

    public int getErrorState();

    public void clear();

}

package com.ilargia.games.egdx.interfaces;


public interface Game {

    public void init(String [] args);

    public int runGame();

    public void dispose();

    public void pushState(GameState state);

    public GameState popState();

    public void changeState(GameState state);

    public boolean isRunning();

    public int getErrorState();

    public void clear();

}

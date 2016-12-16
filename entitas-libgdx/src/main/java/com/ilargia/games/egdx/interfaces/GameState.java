package com.ilargia.games.egdx.interfaces;


public interface GameState {

    public void loadResources();

    public void init();

    public void onResume();

    public void onPause();

    public void unloadResources();

}

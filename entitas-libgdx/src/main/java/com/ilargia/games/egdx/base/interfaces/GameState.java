package com.ilargia.games.egdx.base.interfaces;

public interface GameState<E extends Engine> {

    public void setEngine(E engine);

    public void loadResources();

    public void init();

    public void onResume();

    public void onPause();

    public void unloadResources();

}

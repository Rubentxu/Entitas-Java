package com.ilargia.games.egdx.interfaces;


public interface GameState<E extends Engine> {

    public void loadResources(E engine);

    public void init(E engine);

    public void onResume(E engine);

    public void onPause(E engine);

    public void unloadResources(E engine);

}

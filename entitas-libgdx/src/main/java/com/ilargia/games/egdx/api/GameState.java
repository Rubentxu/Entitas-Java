package com.ilargia.games.egdx.api;

public interface GameState {

    public void loadResources();

    public void init();

    public void onResume();

    public void update(float deltaTime);

    public void render();

    public void onPause();

    public void dispose();

    public void unloadResources();

}

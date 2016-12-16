package com.ilargia.games.egdx;


import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.GameState;

public class EGGameState implements GameState{
    private Engine _engine;

    public EGGameState(Engine engine) {
        this._engine = engine;
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void init() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {

    }
}

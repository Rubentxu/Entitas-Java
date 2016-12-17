package com.ilargia.games.egdx;


import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.Game;
import com.ilargia.games.egdx.interfaces.GameState;

import java.util.Stack;

public class EGGame implements Game {

    private Stack<GameState> _states;
    private Engine _engine;
    private int thisTime = 0;
    private int lastTime = 0;

    public EGGame(Engine engine) {
        this._engine = engine;
    }

    @Override
    public void init(String[] args) {
        _engine.configure(args);
        _engine.initSystems();
    }

    @Override
    public int runGame() {
        return 0;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void pushState(GameState state) {

    }

    @Override
    public GameState popState() {
        return null;
    }

    @Override
    public void changeState(GameState state) {

    }

    @Override
    public boolean isRunning() {
        return !_engine.hasShutdown();
    }

    @Override
    public int getErrorState() {
        return _engine.getErrorState();
    }

    @Override
    public void clear() {

    }
}

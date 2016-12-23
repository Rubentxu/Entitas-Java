package com.ilargia.games.egdx;


import com.badlogic.gdx.Gdx;
import com.ilargia.games.egdx.events.game.GameEvent;
import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.EventBus;
import com.ilargia.games.egdx.interfaces.Game;
import com.ilargia.games.egdx.interfaces.GameState;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.engio.mbassy.bus.MBassador;
import net.engio.mbassy.listener.Handler;

public abstract class EGGame implements Game {

    private ObjectArrayList<GameState> _states;
    private Engine _engine;
    private int thisTime = 0;
    private int lastTime = 0;
    public static EventBus ebus;

    public EGGame(Engine engine) {
        this._engine = engine;
        this._states = new ObjectArrayList<>();
        this.ebus = new EGEventBus(new MBassador());
    }

    @Override
    public void init(String[] args) {
        _engine.configure(args);
        _engine.init();
    }

    @Override
    public void runGame() {
        _engine.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        _states.clear();
        _states = null;
        _engine = null;

    }

    @Override
    public void pushState(GameState state) {
        state.loadResources(_engine);
        state.init(_engine);
        state.onResume(_engine);
        _states.push(state);
    }

    @Override
    public GameState popState() {
        GameState state = _states.pop();
        state.onPause(_engine);
        state.unloadResources(_engine);
        return state;
    }

    @Override
    public GameState changeState(GameState state) {
        GameState oldState = popState();
        pushState(state);
        return oldState;
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
        _states.clear();
    }



}

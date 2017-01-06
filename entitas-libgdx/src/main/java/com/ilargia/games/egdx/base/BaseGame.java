package com.ilargia.games.egdx.base;


import com.ilargia.games.egdx.base.interfaces.Engine;
import com.ilargia.games.egdx.base.interfaces.EventBus;
import com.ilargia.games.egdx.base.interfaces.Game;
import com.ilargia.games.egdx.base.interfaces.GameState;
import com.ilargia.games.entitas.Systems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class BaseGame<E extends Engine> implements Game<E> {

    private ObjectArrayList<GameState> _states;
    private E _engine;
    public Systems _systems;
    public static EventBus ebus;

    public BaseGame(E engine, Systems systems, EventBus bus) {
        this._engine = engine;
        this._systems = systems;
        this._states = new ObjectArrayList<>();
        this.ebus = bus;
    }

    @Override
    public void update(float deltaTime) {
        _systems.execute(deltaTime);
        _systems.render();

    }

    @Override
    public void dispose() {
        _states.clear();
        _systems.clearSystems();
        _states = null;
        _engine = null;
        _systems = null;

    }

    @Override
    public void pushState(GameState<E> state) {
        state.setEngine(_engine);
        state.loadResources();
        state.init();
        state.onResume();
        _states.push(state);
    }

    @Override
    public GameState<E> popState() {
        GameState<E> state = _states.pop();
        state.onPause();
        state.unloadResources();
        return state;
    }

    @Override
    public GameState<E> changeState(GameState<E> state) {
        GameState<E> oldState = popState();
        pushState(state);
        return oldState;
    }


    @Override
    public void clear() {
        _states.clear();
    }



}

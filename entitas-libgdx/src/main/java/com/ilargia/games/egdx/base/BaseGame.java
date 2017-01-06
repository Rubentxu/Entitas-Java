package com.ilargia.games.egdx.base;


import com.ilargia.games.egdx.base.interfaces.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class BaseGame<E extends Engine> implements Game {

    public static EventBus ebus;
    protected ObjectArrayList<GameState> _states;
    protected E _engine;

    public BaseGame(E engine, EventBus bus) {
        this._engine = engine;
        this._states = new ObjectArrayList<>();
        this.ebus = bus;
    }

    @Override
    public void update(float deltaTime) {
        _states.top().update(deltaTime);
        _states.top().render();
    }

    @Override
    public void dispose() {
        _states.clear();
        _states = null;
        _engine = null;

    }

    @Override
    public void pushState(GameState state) {
        state.loadResources();
        state.init();
        state.onResume();
        _states.push(state);
    }

    @Override
    public GameState popState() {
        GameState state = _states.pop();
        state.onPause();
        state.unloadResources();
        return state;
    }

    @Override
    public GameState changeState(GameState state) {
        GameState oldState = popState();
        pushState(state);
        return oldState;
    }

    @Override
    public void changeState(GameState state, StateTransition transition) {
        GameState oldState = _states.pop();
        transition.states(this, oldState , state);
    }


    @Override
    public void clear() {
        _states.clear();
    }


}

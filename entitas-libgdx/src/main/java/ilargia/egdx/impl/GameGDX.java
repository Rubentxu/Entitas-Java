package ilargia.egdx.impl;


import ilargia.egdx.api.*;
import ilargia.entitas.factories.EntitasCollections;
import java.util.Stack;


public abstract class GameGDX<E extends Engine> implements Game<E> {

    public static EventBus ebus;
    protected Stack<GameState> _states;
    protected E _engine;

    public GameGDX(E engine, EventBus bus) {
        this._engine = engine;
        this._states = EntitasCollections.createStack(GameState.class);
        this.ebus = bus;
    }

    @Override
    public void update(float deltaTime) {
        _engine.update(deltaTime);
        _states.peek().update(deltaTime);
        _states.peek().render();
    }

    @Override
    public void dispose() {
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
        state.dispose();
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
        transition.states(this, oldState, state);
    }


    @Override
    public void clear() {
        _states.clear();
    }

    @Override
    public E getEngine() {
        return _engine;
    }


}

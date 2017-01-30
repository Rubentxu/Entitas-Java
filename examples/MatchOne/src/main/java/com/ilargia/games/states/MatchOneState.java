package com.ilargia.games.states;

import com.ilargia.games.MatchOneEngine;
import com.ilargia.games.core.Entitas;
import com.ilargia.games.egdx.base.BaseGameState;


public class MatchOneState extends BaseGameState {

    private final MatchOneEngine engine;
    private final Entitas context;

    public MatchOneState(MatchOneEngine engine) {
        this.engine = engine;
        context = new Entitas();
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {
        context.game.destroyAllEntities();
        systems.clearSystems();
    }


}

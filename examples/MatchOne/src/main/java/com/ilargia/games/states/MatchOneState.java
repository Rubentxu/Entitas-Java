package com.ilargia.games.states;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.MatchOneEngine;
import com.ilargia.games.core.Entitas;
import com.ilargia.games.egdx.base.BaseGameState;


public class MatchOneState extends BaseGameState {

    private final MatchOneEngine engine;
    private final Entitas context;
    private final World physic;


    public MatchOneState(MatchOneEngine engine) {
        this.engine = engine;
        context = new Entitas();
        physic = new World(new Vector2(0, -9.81f),true);
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

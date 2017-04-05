package com.ilargia.games.egdx.logicbricks.system.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.InputManagerGDX;
import com.ilargia.games.egdx.impl.managers.PreferencesManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class DestroySystem extends ReactiveSystem<GameEntity> implements IInitializeSystem, ICleanupSystem {
    private final List<Body> removeBodies;
    private final EngineGDX engine;
    private GameContext context;
    private World physics;
    private InputManagerGDX inputManager;
    private PreferencesManagerGDX preferences;

    public DestroySystem(Entitas entitas, EngineGDX engine) {
        super(entitas.game);
        this.engine = engine;
        this.physics = engine.getPhysics();
        this.context = entitas.game;
        this.removeBodies = EntitasCollections.createList(Body.class);
    }


    @Override
    public void initialize() {
        inputManager = engine.getManager(InputManagerGDX.class);
        preferences = engine.getManager(PreferencesManagerGDX.class);
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.Destroy());
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            if (e.hasRigidBody()) {
                Body body = e.getRigidBody().body;
                e.getRigidBody().body = null;
                body.setUserData(null);
                removeBodies.add(body);

            }
            context.destroyEntity(e);

        }
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.isDestroy();
    }

    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), preferences.VELOCITY_ITERATIONS, preferences.POSITION_ITERATIONS);
        for (Body removeBody : removeBodies) {
            for (int i = 0; i < 5; i++) {
                if(inputManager.getTouchState(i).joint != null) {
                    physics.destroyJoint(inputManager.getTouchState(i).joint);
                    inputManager.getTouchState(i).joint = null;
                }

            }
            physics.destroyBody(removeBody);
        }
        removeBodies.clear();
    }

}

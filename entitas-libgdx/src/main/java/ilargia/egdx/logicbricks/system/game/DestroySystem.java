package ilargia.egdx.logicbricks.system.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.impl.managers.InputManagerGDX;
import ilargia.egdx.impl.managers.PreferencesManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameContext;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.game.GameMatcher;
import ilargia.entitas.api.IContext;
import ilargia.entitas.api.system.ICleanupSystem;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.factories.EntitasCollections;
import ilargia.entitas.systems.ReactiveSystem;

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
                if(inputManager.joints[i] != null) {
                    physics.destroyJoint(inputManager.joints[i]);
                    inputManager.joints[i] = null;
                }

            }
            physics.destroyBody(removeBody);
        }
        removeBodies.clear();
    }

}

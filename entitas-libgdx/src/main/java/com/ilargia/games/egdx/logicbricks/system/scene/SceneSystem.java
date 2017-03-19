package com.ilargia.games.egdx.logicbricks.system.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.impl.managers.PreferencesManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class SceneSystem extends ReactiveSystem<GameEntity> implements IInitializeSystem, ICleanupSystem {

    public static int BOX2D_VELOCITY_ITERATIONS;
    public static int BOX2D_POSITION_ITERATIONS;
    private final SceneContext context;
    private final Engine engine;
    private World physics;


    public SceneSystem(Engine engine, Entitas entitas) {
        super(entitas.game);
        this.engine = engine;
        this.physics = engine.getManager(PhysicsManagerGDX.class).getPhysics();
        this.context = entitas.scene;
    }

    @Override
    protected Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return context.createCollector(Matcher.AllOf(GameMatcher.Tags(), GameMatcher.RigidBody()));
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasTags() && (!entity.hasTextureView() || !entity.hasRigidBody());
    }


    @Override
    public void initialize() {
        PreferencesManagerGDX preferences = engine.getManager(PreferencesManagerGDX.class);
        BOX2D_VELOCITY_ITERATIONS = preferences.VELOCITY_ITERATIONS;
        BOX2D_POSITION_ITERATIONS = preferences.POSITION_ITERATIONS;
        context.setGameWorld(preferences.GAME_WIDTH, preferences.GAME_HEIGHT, 64, Color.BLUE);

        Camera camera = context.getCamera().camera;
        camera.position.set(preferences.GAME_WIDTH / 2, preferences.GAME_HEIGHT / 2, 0);
        camera.viewportWidth = preferences.GAME_WIDTH;
        camera.viewportHeight = preferences.GAME_HEIGHT;

    }

    @Override
    protected void execute(List<GameEntity> gameEntities) {
        for (GameEntity gameEntity : gameEntities) {
            //  entities.get(gameEntity.getIdentity().type).create(null);
        }
    }

    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), BOX2D_VELOCITY_ITERATIONS, BOX2D_POSITION_ITERATIONS);
    }


}

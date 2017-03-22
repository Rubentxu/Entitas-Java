package com.ilargia.games.egdx.logicbricks.system.scene;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.LogManagerGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.impl.managers.PreferencesManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneEntity;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneMatcher;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class SceneSystem extends ReactiveSystem<SceneEntity> implements IInitializeSystem, ICleanupSystem {

    public static int BOX2D_VELOCITY_ITERATIONS;
    public static int BOX2D_POSITION_ITERATIONS;
    private final SceneContext context;
    private final Engine engine;
    private World physics;
    private SceneManagerGDX sceneManager;
    private OrthographicCamera camera;

    public SceneSystem(Entitas entitas, Engine engine) {
        super(entitas.scene);
        this.engine = engine;
        this.context = entitas.scene;

    }

    @Override
    public void initialize() {
        this.physics = engine.getManager(PhysicsManagerGDX.class).getPhysics();
        PreferencesManagerGDX preferences = engine.getManager(PreferencesManagerGDX.class);
        sceneManager = engine.getManager(SceneManagerGDX.class);
        BOX2D_VELOCITY_ITERATIONS = preferences.VELOCITY_ITERATIONS;
        BOX2D_POSITION_ITERATIONS = preferences.POSITION_ITERATIONS;
        context.setGameWorld(preferences.GAME_WIDTH, preferences.GAME_HEIGHT, 64, Color.BLUE);

        camera = (OrthographicCamera) context.getCamera().camera;
        camera.position.set(preferences.GAME_WIDTH / 2, preferences.GAME_HEIGHT / 2, 0);
        camera.viewportWidth = preferences.GAME_WIDTH;
        camera.viewportHeight = preferences.GAME_HEIGHT;

    }

    @Override
    protected Collector<SceneEntity> getTrigger(IContext<SceneEntity> context) {
        return context.createCollector(SceneMatcher.PositionalLight());
    }

    @Override
    protected boolean filter(SceneEntity entity) {
        return entity.hasPositionalLight();
    }

    @Override
    protected void execute(List<SceneEntity> entities) {
        for (SceneEntity entity : entities) {
            sceneManager.createLight(PointLight.class,entity.getPositionalLight());
        }
    }

    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), BOX2D_VELOCITY_ITERATIONS, BOX2D_POSITION_ITERATIONS);
    }

}

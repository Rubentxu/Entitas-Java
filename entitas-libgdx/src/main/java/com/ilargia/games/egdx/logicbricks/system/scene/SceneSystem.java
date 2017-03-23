package com.ilargia.games.egdx.logicbricks.system.scene;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
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
import com.ilargia.games.egdx.logicbricks.system.render.LigthRendererSystem;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class SceneSystem extends ReactiveSystem<SceneEntity> implements IInitializeSystem, ICleanupSystem {

    private final SceneContext context;
    private final Engine engine;
    private World physics;
    private SceneManagerGDX sceneManager;
    private OrthographicCamera camera;
    private float physicsTimeLeft;
    private PreferencesManagerGDX preferences;

    public SceneSystem(Entitas entitas, Engine engine) {
        super(entitas.scene);
        this.engine = engine;
        this.context = entitas.scene;

    }

    @Override
    public void initialize() {
        this.physics = engine.getManager(PhysicsManagerGDX.class).getPhysics();
        preferences = engine.getManager(PreferencesManagerGDX.class);
        sceneManager = engine.getManager(SceneManagerGDX.class);
        context.setGameWorld(preferences.GAME_WIDTH, preferences.GAME_HEIGHT, 64, Color.BLUE);

        camera = (OrthographicCamera) context.getCamera().camera;
        camera.position.set(preferences.GAME_WIDTH / 2, preferences.GAME_HEIGHT / 2, 0);
        camera.viewportWidth = preferences.GAME_WIDTH;
        camera.viewportHeight = preferences.GAME_HEIGHT;

    }

    @Override
    protected Collector<SceneEntity> getTrigger(IContext<SceneEntity> context) {
        return context.createCollector(Matcher.AnyOf(SceneMatcher.CDirectionalLight(),SceneMatcher.CPointLight(),
                SceneMatcher.CChainLight(), SceneMatcher.CConeLight()));
    }

    @Override
    protected boolean filter(SceneEntity entity) {
        return entity.hasCPointLight() || entity.hasCDirectionalLight() || entity.hasCChainLight()|| entity.hasCConeLight();
    }

    @Override
    protected void execute(List<SceneEntity> entities) {

        for (SceneEntity entity : entities) {
            if(entity.hasCPointLight()) {
                sceneManager.createLight(PointLight.class, entity.getCPointLight());
                LogManagerGDX.debug("SceneSystem", "create positional ligth, num entities: %d", entities.size());
            }
            if(entity.hasCDirectionalLight()) {
                sceneManager.createLight(DirectionalLight.class, entity.getCDirectionalLight());
                LogManagerGDX.debug("SceneSystem", "create directional ligth");
            }
            if(entity.hasCChainLight()) {
                sceneManager.createLight(ChainLight.class, entity.getCChainLight());
                LogManagerGDX.debug("SceneSystem", "create chained ligth");
            }
            if(entity.hasCConeLight()) {
                sceneManager.createLight(ConeLight.class, entity.getCConeLight());
                LogManagerGDX.debug("SceneSystem", "create cone ligth");
            }
        }
    }

    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), preferences.VELOCITY_ITERATIONS, preferences.POSITION_ITERATIONS);

    }

}

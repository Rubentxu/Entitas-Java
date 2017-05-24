package ilargia.egdx.logicbricks.system.scene;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import ilargia.egdx.api.Engine;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.impl.managers.PreferencesManagerGDX;
import ilargia.egdx.impl.managers.SceneManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.scene.SceneContext;
import ilargia.egdx.logicbricks.gen.scene.SceneEntity;
import ilargia.egdx.logicbricks.gen.scene.SceneMatcher;
import ilargia.entitas.api.IContext;
import ilargia.entitas.api.system.ICleanupSystem;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.matcher.Matcher;
import ilargia.entitas.systems.ReactiveSystem;

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
        camera.position.set(preferences.GAME_WIDTH / 2, preferences.GAME_HEIGHT  /2 +4, 0);
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
            }
            if(entity.hasCDirectionalLight()) {
                sceneManager.createLight(DirectionalLight.class, entity.getCDirectionalLight());
            }
            if(entity.hasCChainLight()) {
                sceneManager.createLight(ChainLight.class, entity.getCChainLight());
            }
            if(entity.hasCConeLight()) {
                sceneManager.createLight(ConeLight.class, entity.getCConeLight());
            }
        }
    }

    @Override
    public void cleanup() {


    }

}

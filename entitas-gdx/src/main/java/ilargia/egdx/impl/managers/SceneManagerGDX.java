package ilargia.egdx.impl.managers;

import box2dLight.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ilargia.egdx.api.factories.EntityFactory;
import ilargia.egdx.api.factories.LightFactory;
import ilargia.egdx.api.factories.SceneFactory;
import ilargia.egdx.api.managers.SceneManager;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.logicbricks.component.scene.CChainLight;
import ilargia.egdx.logicbricks.component.scene.CConeLight;
import ilargia.egdx.logicbricks.component.scene.CDirectionalLight;
import ilargia.egdx.logicbricks.component.scene.CPointLight;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.util.MapEntityParser;
import ilargia.entitas.Entity;
import ilargia.entitas.api.entitas.EntitasException;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Map;

public class SceneManagerGDX implements SceneManager {

    private final Entitas entitas;
    public EngineGDX engine;
    public PhysicsManagerGDX physics;
    public Map<String, EntityFactory> entityFactories;
    public Map<String, SceneFactory> sceneFactories;
    public Map<Class<?>, LightFactory<SceneManagerGDX,IComponent,? extends Light>> ligthFactories;
    public RayHandler rayHandler;
    protected Batch batch;
    protected Camera defaultCamera;
    protected MapEntityParser mapParser;

    public SceneManagerGDX(EngineGDX engine, Entitas entitas) {
        this.engine = engine;
        this.entitas = entitas;
        this.entityFactories = EntitasCollections.createMap(String.class, EntityFactory.class);
        this.sceneFactories = EntitasCollections.createMap(String.class, SceneFactory.class);
        this.ligthFactories = EntitasCollections.createMap(String.class, LightFactory.class);
        batch = new SpriteBatch();
        defaultCamera = createCamera("Orthographic");
        mapParser = new MapEntityParser(this);
    }

    @Override
    public void initialize() {
        if (engine.getManager(PhysicsManagerGDX.class) == null) throw new EntitasException("SceneManagerGDX",
                "SceneManagerGDX needs load PhysicsManagerGDX on the engine");
        physics = engine.getManager(PhysicsManagerGDX.class);
        rayHandler = new RayHandler(physics.getPhysics());
        PreferencesManagerGDX preferences = engine.getManager(PreferencesManagerGDX.class);
        rayHandler.setAmbientLight(preferences.AMBIENT_LIGHT);
        rayHandler.setBlur(preferences.BLUR);
        rayHandler.setCulling(preferences.CULLING);
        rayHandler.setGammaCorrection(preferences.GAMMA_CORRECTION);
        rayHandler.setBlurNum(preferences.BLUR_NUM);
        rayHandler.setShadows(preferences.SHADOWS);
        rayHandler.useDiffuseLight(preferences.USE_DIFFUSE_LIGHT);
        //rayHandler.update();

        AssetsManagerGDX assetsManager = engine.getManager(AssetsManagerGDX.class);
        for (EntityFactory factory : entityFactories.values()) {
            factory.loadAssets(engine);
        }
        assetsManager.finishLoading();

        addLightFactory(PointLight.class,(SceneManager sceneManager, IComponent c)-> {
            CPointLight component = (CPointLight) c;
            return new PointLight(rayHandler, component.raysNum, component.color, component.distance, component.position.x,
                    component.position.y);
        });

        addLightFactory(DirectionalLight.class,(SceneManager sceneManager, IComponent c)-> {
            LogManagerGDX.debug("SceneManager", "create directional ligth");
            CDirectionalLight component = (CDirectionalLight) c;
            return new DirectionalLight(rayHandler, component.raysNum, component.color, component.direcction);
        });

        addLightFactory(ChainLight.class,(SceneManager sceneManager, IComponent c)-> {
            LogManagerGDX.debug("SceneManager", "create ChainLight");
            CChainLight component = (CChainLight) c;
            return new ChainLight(rayHandler, component.raysNum, component.color, component.distance, component.rayDirecction,
                    component.chain);
        });

        addLightFactory(ConeLight.class,(SceneManager sceneManager, IComponent c)-> {
            LogManagerGDX.debug("SceneManager", "create ConeLight");
            CConeLight component = (CConeLight) c;
            return new ConeLight(rayHandler, component.raysNum, component.color, component.distance, component.position.x,
                    component.position.y, component.directionDegree, component.coneDegree);
        });
    }

    @Override
    public void addEntityFactory(String name, EntityFactory factory) {
        entityFactories.put(name, factory);
    }

    @Override
    public <TEntity extends Entity> TEntity createEntity(String name) {
        EntityFactory<Entitas,TEntity> factory = entityFactories.get(name);
        TEntity entity = null;
        if (factory != null) {
            entity = factory.create(engine, entitas);
        }
        return entity;

    }

    @Override
    public void addSceneFactory(String name, SceneFactory factory) {
        sceneFactories.put(name, factory);
    }

    @Override
    public <L> void addLightFactory(Class<L> type, LightFactory factory) {
        ligthFactories.put(type, factory);
    }

    @Override
    public void createScene(String scene) {
        SceneFactory<EngineGDX, Entitas> factory = sceneFactories.get(scene);
        if(factory !=null) {
            factory.createScene(engine, entitas);
        }
    }

    @Override
    public <C extends IComponent, L> L createLight(Class<L> type, C lightComponent) {
        LightFactory<SceneManagerGDX, IComponent, ? extends Light> factory = ligthFactories.get(type);
        if(factory !=null) {
            return (L) factory.createLigth(this, lightComponent);
        }
        return null;
    }

    @Override
    public Camera createCamera(String type) {
        if (type.equals("Orthographic"))
            return new OrthographicCamera();
        else
            return new PerspectiveCamera();
    }

    @Override
    public Camera getDefaultCamera() {
        return defaultCamera;
    }

    @Override
    public Batch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
        entityFactories.clear();
        defaultCamera = null;
        engine = null;
    }

}

package com.ilargia.games.egdx.impl.managers;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ilargia.games.egdx.api.factories.EntityFactory;
import com.ilargia.games.egdx.api.factories.SceneFactory;
import com.ilargia.games.egdx.api.managers.SceneManager;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.util.MapEntityParser;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.EntitasException;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class SceneManagerGDX implements SceneManager {

    private final Entitas entitas;
    public EngineGDX engine;
    public PhysicsManagerGDX physics;
    public Map<String, EntityFactory> entityFactories;
    public Map<String, SceneFactory> sceneFactories;
    public RayHandler rayHandler;
    protected Batch batch;
    protected Camera defaultCamera;
    protected MapEntityParser mapParser;

    public SceneManagerGDX(EngineGDX engine, Entitas entitas) {
        this.engine = engine;
        this.entitas = entitas;
        this.entityFactories = EntitasCollections.createMap(String.class, EntityFactory.class);
        this.sceneFactories = EntitasCollections.createMap(String.class, SceneFactory.class);
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
        AssetsManagerGDX assetsManager = engine.getManager(AssetsManagerGDX.class);
        for (EntityFactory factory : entityFactories.values()) {
            factory.loadAssets(engine);
        }
        assetsManager.finishLoading();
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
    public void createScene(String scene) {
        SceneFactory<EngineGDX, Entitas> factory = sceneFactories.get(scene);
        if(factory !=null) {
            factory.createScene(engine, entitas);
        }
    }

    @Override
    public Light createLight(String type, int raysNum, int distance, int colorRGBA) {
        Light light = null;
        if (type.equals("PointLight"))
            light = new PointLight(rayHandler, raysNum, new Color(colorRGBA), distance, 0, 0);
        return light;
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

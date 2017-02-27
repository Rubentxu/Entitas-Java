package com.ilargia.games.egdx.base.managers;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.egdx.api.managers.SceneManager;
import com.ilargia.games.egdx.base.BaseEngine;
import com.ilargia.games.egdx.util.MapEntityParser;
import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.EntitasException;
import com.ilargia.games.entitas.factories.CollectionFactories;

import java.util.Map;

public class BaseSceneManager implements SceneManager {

    public BasePhysicsManager physics;
    public Map<String, EntityFactory> factories;
    public BaseEngine engine;
    public RayHandler rayHandler;
    protected Batch batch;
    protected Camera defaultCamera;
    protected MapEntityParser mapParser;

    public BaseSceneManager(BaseEngine engine) {
        this.engine = engine;
        this.factories = CollectionFactories.createMap(String.class, EntityFactory.class);
        if(engine.getManager(BasePhysicsManager.class)== null) throw new EntitasException("BaseSceneManager",
                "BaseSceneManager needs to first load BasePhysicsManager on the engine");
        physics = engine.getManager(BasePhysicsManager.class);
        rayHandler = new RayHandler(physics.getPhysics());
        batch = new SpriteBatch();
        defaultCamera = createCamera("Orthographic");
        mapParser =  new MapEntityParser(this);
    }

    @Override
    public void initialize() {
        BaseAssetsManager assetsManager = engine.getManager(BaseAssetsManager.class);
        for (EntityFactory factory : factories.values()) {
            factory.loadAssets(engine);
        }
        assetsManager.finishLoading();
    }

    @Override
    public void addEntityFactory(String name, EntityFactory factory) {
        factories.put(name, factory);
    }

    @Override
    public <TEntity extends Entity> TEntity createEntity(String name) {
        EntityFactory<TEntity> factory = factories.get(name);
        TEntity entity = null;
        if (factory != null) {
            entity = factory.create(engine);
        }
        return entity;

    }

    @Override
    public Light createLight(String type, int raysNum, int distance, int colorRGBA) {
        Light light = null;
        if (type.equals("PointLight")) light = new PointLight(rayHandler, raysNum, new Color(colorRGBA), distance, 0, 0);
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
    public void createScene(String scene) {
        mapParser.load(scene);
    }


    @Override
    public void dispose() {
        batch.dispose();
        factories.clear();
        defaultCamera = null;
        engine = null;
    }

}

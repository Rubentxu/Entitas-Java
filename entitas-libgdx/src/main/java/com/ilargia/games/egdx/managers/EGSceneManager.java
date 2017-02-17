package com.ilargia.games.egdx.managers;

import box2dLight.Light;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.egdx.api.managers.SceneManager;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;

public abstract class EGSceneManager implements SceneManager<TiledMap> {
    private Map<String, EntityFactory> factories;

    public EGSceneManager() {
        factories = Collections.createMap(String.class, EntityFactory.class);
    }

    @Override
    public void addEntityFactory(String name, EntityFactory factory) {
        factories.put(name, factory);
    }

    @Override
    public Light createLight() {
        return null;
    }

    @Override
    public Camera createCamera() {
        return new OrthographicCamera();
    }


    @Override
    public void dispose() {

    }

}

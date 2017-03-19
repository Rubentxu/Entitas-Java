package com.ilargia.games.egdx.impl;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.managers.Manager;
import com.ilargia.games.egdx.api.managers.PhysicsManager;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IContexts;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class EngineGDX implements Engine, IContexts {

    private final EntitasCollections collectionsImpl;
    public Map<Class<? extends Manager>, Manager> _managers;

    public EngineGDX(CollectionsFactories factories) {
        collectionsImpl = new EntitasCollections(factories);
        _managers = EntitasCollections.createMap(Class.class, Manager.class);
    }

    @Override
    public void dispose() {
        for (Manager manager : _managers.values()) {
            manager.dispose();
        }
        _managers.clear();
        _managers = null;

    }

    @Override
    public <M extends Manager> M getManager(Class<M> clazz) {
        return (M) _managers.get(clazz);

    }

    @Override
    public <M extends Manager> Engine addManager(M manager) {
        manager.initialize();
        _managers.put(manager.getClass(), manager);
        return this;
    }

    @Override
    public IContext[] allContexts() {
        return new IContext[0];
    }

    public World getPhysics() {
        return getManager(PhysicsManagerGDX.class).getPhysics();
    }

    public BodyBuilder getBodyBuilder() {
        return getManager(PhysicsManagerGDX.class).getBodyBuilder();
    }

    public Batch getBatch() {
        return getManager(SceneManagerGDX.class).getBatch();
    }

    public Camera getCamera() {
        return getManager(SceneManagerGDX.class).getDefaultCamera();
    }

}

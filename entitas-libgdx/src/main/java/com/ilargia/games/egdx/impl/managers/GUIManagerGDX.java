package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.GUIFactory;
import com.ilargia.games.egdx.api.managers.GUIManager;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class GUIManagerGDX implements GUIManager<AssetsManagerGDX> {
    public EngineGDX engine;
    protected Map<String, GUIFactory> guiFactories;
    protected BitmapFont defaultFont;
    protected Skin skin;

    public GUIManagerGDX(BitmapFont defaultFont, Skin skin, EngineGDX engine) {
        this.guiFactories = EntitasCollections.createMap(String.class, GUIFactory.class);
        this.defaultFont = defaultFont;
        this.skin = skin;
        this.engine = engine;
    }

    @Override
    public Skin getSkin() {
        return skin;
    }


    @Override
    public void addGUIFactory(String name, GUIFactory factory) {
        guiFactories.put(name, factory);
    }

    @Override
    public <TEntity extends IEntity> TEntity createGUIElement(String name, float posX, float posY) {
        GUIFactory<TEntity> factory = guiFactories.get(name);
        TEntity entity = null;
        if (factory != null) {
            entity = factory.create(engine);
        }
        return entity;
    }


    @Override
    public void initialize() {
        AssetsManagerGDX assetsManager = engine.getManager(AssetsManagerGDX.class);
        for (GUIFactory factory : guiFactories.values()) {
            factory.loadAssets(engine);
        }
        assetsManager.finishLoading();
    }

    @Override
    public void dispose() {
        skin.dispose();
        defaultFont.dispose();
        guiFactories.clear();
        guiFactories = null;
    }
}

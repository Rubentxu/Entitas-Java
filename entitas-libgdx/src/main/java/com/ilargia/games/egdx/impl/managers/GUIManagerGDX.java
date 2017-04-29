package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ilargia.games.egdx.api.factories.GUIFactory;
import com.ilargia.games.egdx.api.managers.GUIManager;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class GUIManagerGDX implements GUIManager<AssetsManagerGDX,Actor> {
    public EngineGDX engine;
    protected Map<String, GUIFactory> guiFactories;
    protected BitmapFont defaultFont;
    protected Skin skin;
    protected Stage stage;

    public GUIManagerGDX(Viewport viewport, BitmapFont defaultFont, Skin skin, EngineGDX engine) {
        this.guiFactories = EntitasCollections.createMap(String.class, GUIFactory.class);
        this.defaultFont = defaultFont;
        this.skin = skin;
        this.engine = engine;
        stage = new Stage(viewport);

    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }


    @Override
    public void addGUIFactory(String name, GUIFactory factory) {
        guiFactories.put(name, factory);
    }

    @Override
    public Actor createGUIElement(String name) {
        GUIFactory<Actor,GUIManagerGDX> factory = guiFactories.get(name);
        Actor element = null;
        if (factory != null) {
            element = factory.create(this);
        }
        return element;
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

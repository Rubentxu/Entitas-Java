package com.ilargia.games.egdx.base.managers;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.GUIFactory;
import com.ilargia.games.egdx.api.managers.GUIManager;

import java.util.Map;

public abstract class BaseGUIManager implements GUIManager<BaseAssetsManager> {
    protected Map<String, GUIFactory> factories;
    protected BitmapFont defaultFont;
    protected Skin skin;

    @Override
    public Skin getSkin() {
        return skin;
    }

    @Override
    public BitmapFont getDefaultFont() {
        return defaultFont;
    }

    @Override
    public void addGUIFactory(String name, GUIFactory factory) {
        factories.put(name, factory);
    }


    @Override
    public void dispose() {
        skin.dispose();
        defaultFont.dispose();
        factories.clear();
        factories = null;
    }
}

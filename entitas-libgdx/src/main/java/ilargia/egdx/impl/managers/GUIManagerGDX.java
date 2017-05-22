package ilargia.egdx.impl.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import ilargia.egdx.api.factories.GUIFactory;
import ilargia.egdx.api.managers.GUIManager;
import ilargia.egdx.impl.EngineGDX;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Map;

public class GUIManagerGDX implements GUIManager<Actor,Skin, TextureAtlas> {
    public EngineGDX engine;
    protected Map<String, GUIFactory> guiFactories;
    protected BitmapFont defaultFont;
    protected Skin skin;
    protected Stage stage;

    public GUIManagerGDX(Viewport viewport, BitmapFont defaultFont, EngineGDX engine) {
        this.guiFactories = EntitasCollections.createMap(String.class, GUIFactory.class);
        this.defaultFont = defaultFont;
        this.engine = engine;
        stage = new Stage(viewport);

    }

    @Override
    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return stage;
    }


    @Override
    public Skin createSkin(String skinName, TextureAtlas atlas) {
        this.skin = new Skin(Gdx.files.internal(skinName), atlas);
        return skin;
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

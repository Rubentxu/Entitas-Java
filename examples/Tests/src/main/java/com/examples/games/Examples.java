package com.examples.games;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.examples.games.states.PlatformExampleState;
import com.examples.games.util.TestFileHandleResolver;
import com.ilargia.games.egdx.impl.EventBusGDX;
import com.ilargia.games.egdx.impl.managers.*;
import net.engio.mbassy.bus.MBassador;


public class Examples implements ApplicationListener {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    private static ExamplesGame game;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "BASIC";
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;

        new LwjglApplication(new Examples(), config);
    }

    @Override
    public void create() {
        AssetManager assetsManager = new AssetManager(new TestFileHandleResolver());
        PreferencesManagerGDX preferencesManager = new PreferencesManagerGDX();
        ExamplesEngine engine = new ExamplesEngine();
        engine.addManager(new AssetsManagerGDX(assetsManager, preferencesManager));
        engine.addManager(new PhysicsManagerGDX(new Vector2(0,0)));
        engine.addManager(new GUIManagerGDX(new BitmapFont(), null, engine));
        engine.addManager(new SceneManagerGDX(engine));

        game = new ExamplesGame(engine, new EventBusGDX(new MBassador()));
        game.init();
        game.pushState(new PlatformExampleState(engine));

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        game.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {


    }


}
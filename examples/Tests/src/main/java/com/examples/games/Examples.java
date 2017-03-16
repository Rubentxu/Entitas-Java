package com.examples.games;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.examples.games.scenes.SceneManagerExamples;
import com.examples.games.states.PlatformExampleState;
import com.examples.games.util.TestFileHandleResolver;
import com.ilargia.games.egdx.impl.EventBusGDX;
import com.ilargia.games.egdx.impl.managers.*;
import net.engio.mbassy.bus.MBassador;


public class Examples implements ApplicationListener {
    private static PreferencesManagerGDX preferencesManager;
    private static ExamplesGame game;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        preferencesManager = new PreferencesManagerGDX();

        config.title = preferencesManager.APP_NAME;
        config.width = preferencesManager.VIRTUAL_DEVICE_WIDTH;
        config.height = preferencesManager.VIRTUAL_DEVICE_HEIGHT;

        new LwjglApplication(new Examples(), config);
    }

    @Override
    public void create() {
        ExamplesEngine engine = new ExamplesEngine();

        AssetManager assetsManager = new AssetManager(new TestFileHandleResolver());
        engine.addManager(new AssetsManagerGDX(assetsManager, preferencesManager));
        engine.addManager(new PhysicsManagerGDX(new Vector2(0,0)));
        engine.addManager(new GUIManagerGDX(new BitmapFont(), null, engine));
        engine.addManager(new SceneManagerExamples(engine));
        engine.addManager(preferencesManager);

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
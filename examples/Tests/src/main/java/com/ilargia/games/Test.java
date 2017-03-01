package com.ilargia.games;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.egdx.EGEventBus;
import com.ilargia.games.egdx.base.managers.*;
import com.ilargia.games.states.PongState;
import com.ilargia.games.util.TestFileHandleResolver;
import net.engio.mbassy.bus.MBassador;


public class Pong implements ApplicationListener {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 120;
    public static float PLAYER_SPEED = 300;
    private static PongGame game;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "TESTS ENTITAS-LIBGDX";
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;

        new LwjglApplication(new Pong(), config);
    }

    @Override
    public void create() {
        AssetManager assetsManager = new AssetManager(new TestFileHandleResolver());
        BasePreferencesManager preferencesManager = new BasePreferencesManager();
        PongEngine engine = new PongEngine();
        engine.addManager(new BaseAssetsManager(assetsManager, preferencesManager));
        engine.addManager(new BasePhysicsManager(new Vector2(0,0)));
        engine.addManager(new BaseGUIManager(new BitmapFont(), null, engine));
        engine.addManager(new BaseSceneManager(engine));

        game = new PongGame(engine, new EGEventBus(new MBassador()));
        game.init();
        game.pushState(new PongState(engine));

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
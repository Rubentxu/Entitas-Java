package com.ilargia.games;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ilargia.games.egdx.managers.EGAssetsManager;
import com.ilargia.games.egdx.managers.EGPreferencesManager;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.states.SplashState;
import com.ilargia.games.util.TestFileHandleResolver;


public class Pong implements ApplicationListener {
    private static PongGame game;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 120;
    public static float PLAYER_SPEED = 300;


    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "BASIC";
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;

        new LwjglApplication(new Pong(), config);
    }

    @Override
    public void create() {
        AssetManager assetsManager = new AssetManager(new TestFileHandleResolver());
        EGPreferencesManager preferencesManager =  new EGPreferencesManager();
        PongEngine engine = new PongEngine(new Systems(), new EGAssetsManager(assetsManager, preferencesManager));
        game = new PongGame(engine);
        game.init(null);
        game.pushState(new SplashState());

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        game.runGame();
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
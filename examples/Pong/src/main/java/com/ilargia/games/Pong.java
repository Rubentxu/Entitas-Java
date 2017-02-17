package com.ilargia.games;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ilargia.games.egdx.EGEventBus;
import com.ilargia.games.egdx.managers.EGAssetsManager;
import com.ilargia.games.egdx.managers.EGPreferencesManager;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.states.SplashState;
import com.ilargia.games.util.TestFileHandleResolver;
import net.engio.mbassy.bus.MBassador;

import java.util.*;


public class Pong implements ApplicationListener {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 120;
    public static float PLAYER_SPEED = 300;
    private static PongGame game;

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
        EGPreferencesManager preferencesManager = new EGPreferencesManager();
        PongEngine engine = new PongEngine();
        engine.addManager(new EGAssetsManager(assetsManager, preferencesManager));
        new Collections(new CollectionsFactory() {
            @Override
            public <T> List createList(Class<T> clazz) {
                return new ArrayList<T>();
            }

            @Override
            public <T> Set createSet(Class<T> clazz) {
                return new HashSet<T>();
            }

            @Override
            public <K, V> Map createMap(Class<K> keyClazz, Class<V> valueClazz) {
                return new HashMap();
            }
        });
        game = new PongGame(engine, new EGEventBus(new MBassador()));
        game.init();
        game.pushState(new SplashState(engine));

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
package com.ilargia.games;


import com.badlogic.gdx.ApplicationAdapter;
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
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.engio.mbassy.bus.MBassador;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class MatchOne extends ApplicationAdapter {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 120;
    public static float PLAYER_SPEED = 300;
    private static MatchOneGame game;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "BASIC";
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;

        new LwjglApplication(new MatchOne(), config);
    }

    @Override
    public void create() {
        AssetManager assetsManager = new AssetManager(new TestFileHandleResolver());
        EGPreferencesManager preferencesManager = new EGPreferencesManager();
        MatchOneEngine engine = new MatchOneEngine();
        engine.addManager(new EGAssetsManager(assetsManager, preferencesManager));
        new Collections(new CollectionsFactory() {
            @Override
            public List createList(Class<?> clazz) {
                if(clazz.equals(Integer.class))
                    return new IntArrayList();
                else
                    return new ObjectArrayList();
            }

            @Override
            public Set createSet(Class<?> clazz) {
                if(clazz.equals(Integer.class))
                    return new IntArraySet();
                else
                    return new ObjectOpenHashSet();
            }

            @Override
            public Map createMap(Class<?> keyClazz, Class<?> valueClazz) {
                return new Object2ObjectArrayMap();

            }

        });
        game = new MatchOneGame(engine, new EGEventBus(new MBassador()));
        game.init();
        game.pushState(new SplashState(engine));
    }

    @Override
    public void render() {
        game.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {


    }
}
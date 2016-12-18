package com.ilargia.games;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Player;
import com.ilargia.games.core.Context;
import com.ilargia.games.core.Pool;
import com.ilargia.games.egdx.EGEngine;
import com.ilargia.games.egdx.EGGame;
import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.GameState;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.systems.*;


public class Pong extends ApplicationAdapter {
    EGGame game;

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;
    public static final int PLAYER_WIDTH = 20;
    public static final int PLAYER_HEIGHT = 120;
    public static float PLAYER_SPEED = 300;

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "BASIC";
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;
        new LwjglApplication(new Pong(), config);
    }

    @Override
    public void create() {
        PongEngine engine = new PongEngine(new Systems());
        game = new EGGame(engine);
        PongState state = new PongState();
        game.init(null);
        game.pushState(state);

    }

    @Override
    public void render() {
        game.runGame();
    }

    @Override
    public void dispose() {


    }
}
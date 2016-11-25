package com.ilargia.games;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Bounds;
import com.ilargia.games.components.Player;
import com.ilargia.games.core.Pool;
import com.ilargia.games.core.Pools;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.systems.*;


public class Pong extends ApplicationAdapter {
    private Pools pools;
    private Systems systems;
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 480;

    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "BASIC";
        config.width = SCREEN_WIDTH;
        config.height = SCREEN_HEIGHT;
        new LwjglApplication(new Pong(), config);
    }

    @Override
    public void create() {
        pools =  new Pools();
        Pool core = pools.core;
        ShapeRenderer sr = new ShapeRenderer();
        OrthographicCamera cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        systems = new Systems()
                .add(Pool.createSystem(pools.core,new InputSystem()))
                //.add(Pool.createSystem(pools.core,new ContactSystem()))
                .add(Pool.createSystem(pools.core,new BoundsSystem()))
                .add(Pool.createSystem(pools.core,new MoveSystem()))
                .add(Pool.createSystem(pools.core,new RendererSystem(sr, cam)));

        core.createEntity()
                .addBounds(SCREEN_WIDTH/4f, SCREEN_HEIGHT/2f, 30f,30f, Bounds.Tag.BoundPlayer1);

        core.createEntity()
                .addBall(false)
                .addView(new Circle(20,100,8))
                .addMotion(1,1);

        core.createEntity()
                .addPlayer(Player.ID.PLAYER1)
                .addScore(2)
                .addView(new Rectangle(10,100,20,20))
                .addMotion(0,0);
    }

    @Override
    public void render() {
        systems.execute();
    }

    @Override
    public void dispose() {


    }
}
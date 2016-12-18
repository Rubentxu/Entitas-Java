package com.ilargia.games;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ilargia.games.core.Context;
import com.ilargia.games.egdx.EGEngine;
import com.ilargia.games.egdx.interfaces.Manager;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.systems.*;

public class PongEngine extends EGEngine {

    public Context context;
    public ShapeRenderer sr;
    public Batch batch;
    public BitmapFont font;
    public OrthographicCamera cam;

    public PongEngine(Systems systems, Manager... managers) {
        super(systems, managers);
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void configure(String[] args) {
        context =  new Context();

    }

    @Override
    public void init() {

    }

}

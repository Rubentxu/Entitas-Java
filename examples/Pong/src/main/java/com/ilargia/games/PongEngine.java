package com.ilargia.games;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ilargia.games.egdx.EGEngine;

public class PongEngine extends EGEngine {

    public ShapeRenderer sr;

    public PongEngine() {
        super(new SpriteBatch(), new BitmapFont(), new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        sr = new ShapeRenderer();

    }


}

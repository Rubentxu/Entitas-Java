package com.ilargia.games;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class PongEngine extends EGEngine {

    public ShapeRenderer sr;

    public PongEngine() {
        super(new SpriteBatch(), null, new BitmapFont());
        sr = new ShapeRenderer();

    }


}

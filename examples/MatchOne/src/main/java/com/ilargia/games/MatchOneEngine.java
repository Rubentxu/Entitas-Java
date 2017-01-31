package com.ilargia.games;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.EGEngine;
import com.ilargia.games.util.BodyBuilder;

public class MatchOneEngine extends EGEngine {
    public World physic;
    public BodyBuilder bodyBuilder;

    public MatchOneEngine() {
        super(new SpriteBatch(), new BitmapFont(), new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        physic = new World(new Vector2(0, -9.81f),true);
        bodyBuilder = new BodyBuilder(physic);

    }


}

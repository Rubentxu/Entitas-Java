package com.ilargia.games;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.util.BodyBuilder;

public class MatchOneEngine extends EGEngine {
    public World physic;
    public BodyBuilder bodyBuilder;


    public MatchOneEngine() {
        super(new SpriteBatch(), new World(new Vector2(0,0), true), new BitmapFont());
        camera.position.set((MatchOne.WIDTH / 2) - 0.5f, (MatchOne.HEIGHT / 2) - 0.5f, 0);
        physic = new World(new Vector2(0, -9.81f), true);
        bodyBuilder = new BodyBuilder(physic);

    }


}

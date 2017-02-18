package com.ilargia.games.egdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.base.BaseEngine;
import com.ilargia.games.egdx.managers.EGSceneManager;
import com.ilargia.games.egdx.util.BodyBuilder;


public class EGEngine extends BaseEngine {
    public Batch batch;
    public BitmapFont font;
    public World physics;
    public BodyBuilder bodyBuilder;
    public Camera camera;

    public EGEngine(Batch batch, World world, BitmapFont font) {
        this.batch = batch;
        this.font = font;
        this.physics = world;
        this.bodyBuilder = new BodyBuilder(world);
        this.camera = getManager(EGSceneManager.class).createCamera();

    }
}

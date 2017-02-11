package com.ilargia.games.egdx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ilargia.games.egdx.api.base.BaseEngine;


public class EGEngine extends BaseEngine {
    public Batch batch;
    public BitmapFont font;
    public OrthographicCamera cam;

    public EGEngine(Batch batch, BitmapFont font, OrthographicCamera cam) {
        this.batch = batch;
        this.font = font;
        this.cam = cam;
    }
}

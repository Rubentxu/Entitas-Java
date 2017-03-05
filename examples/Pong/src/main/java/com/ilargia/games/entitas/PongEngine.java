package com.ilargia.games.entitas;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ilargia.games.entitas.egdx.base.BaseEngine;
import com.ilargia.games.entitas.factories.CollectionsFactories;

public class PongEngine extends BaseEngine {

    public ShapeRenderer sr;

    public PongEngine() {
        super(new CollectionsFactories(){});
        sr = new ShapeRenderer();

    }


}

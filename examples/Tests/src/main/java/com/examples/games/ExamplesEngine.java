package com.examples.games;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.entitas.factories.CollectionsFactories;

public class ExamplesEngine extends EngineGDX {

    public ShapeRenderer sr;

    public ExamplesEngine() {
        super(new CollectionsFactories(){});
        sr = new ShapeRenderer();

    }


}

package com.ilargia.games.egdx.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.ilargia.games.egdx.EGEngine;

public class FadeTransition extends RenderTransition {

    public FadeTransition(float duration, Batch batch) {
        super(duration, batch);
    }

    @Override
    public void render() {
        float w = current.getWidth();
        float h = current.getHeight();
        alpha = Interpolation.fade.apply(alpha);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.setColor(1, 1, 1, 1);
        batch.draw(current, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, current.getWidth(), current.getHeight(), false, true);
        batch.setColor(1, 1, 1, alpha);
        batch.draw(next, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, next.getWidth(), next.getHeight(), false, true);
        batch.end();
    }



}

package com.ilargia.games.egdx.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.egdx.EGEngine;

public class SlideTransition extends RenderTransition {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 4;
    private int direction;
    private boolean slideOut;
    private Interpolation easing;

    public SlideTransition(float duration, int direction, boolean slideOut, Interpolation easing, Batch batch) {
        super(duration, batch);
        this.duration = duration;
        this.direction = direction;
        this.slideOut = slideOut;
        this.easing = easing;

    }

    @Override
    public void render() {
        float w = current.getWidth();
        float h = current.getHeight();
        float x = 0;
        float y = 0;
        if (easing != null) alpha = easing.apply(alpha);

        switch (direction) {
            case LEFT:
                x = -w * alpha;
                if (!slideOut) x += w;
                break;
            case RIGHT:
                x = w * alpha;
                if (!slideOut) x -= w;
                break;
            case UP:
                y = h * alpha;
                if (!slideOut) y -= h;
                break;
            case DOWN:
                y = -h * alpha;
                if (!slideOut) y += h;
                break;
        }

        Texture texBottom = slideOut ? next : current;
        Texture texTop = slideOut ? current : next;

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(texBottom, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, current.getWidth(), current.getHeight(), false, true);
        batch.draw(texTop, x, y, 0, 0, w, h, 1, 1, 0, 0, 0, next.getWidth(), next.getHeight(), false, true);
        batch.end();
    }


}

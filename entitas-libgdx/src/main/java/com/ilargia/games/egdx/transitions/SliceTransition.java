package com.ilargia.games.egdx.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.egdx.EGEngine;

public class SliceTransition extends RenderTransition {

    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int UP_DOWN = 3;
    private int direction;
    private Interpolation easing;
    private Array<Integer> sliceIndex = new Array<Integer>();


    public SliceTransition(float duration, int direction, int numSlices, Interpolation easing, EGEngine engine) {
        super(duration, engine);
        this.duration = duration;
        this.direction = direction;
        this.easing = easing;
        this.sliceIndex.clear();
        for (int i = 0; i < numSlices; i++)
            this.sliceIndex.add(i);
        this.sliceIndex.shuffle();

    }

    @Override
    public void render() {
        float w = current.getWidth();
        float h = current.getHeight();
        float x = 0;
        float y = 0;
        int sliceWidth = (int) (w / sliceIndex.size);
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.batch.begin();
        engine.batch.draw(current, 0, 0, 0, 0, w, h, 1, 1, 0, 0, 0, current.getWidth(), current.getHeight(),
                false, true);
        if (easing != null) alpha = easing.apply(alpha);
        for (int i = 0; i < sliceIndex.size; i++) {

            x = i * sliceWidth;

            float offsetY = h * (1 + sliceIndex.get(i) / (float) sliceIndex.size);
            switch (direction) {
                case UP:
                    y = -offsetY + offsetY * alpha;
                    break;
                case DOWN:
                    y = offsetY - offsetY * alpha;
                    break;
                case UP_DOWN:
                    if (i % 2 == 0) {
                        y = -offsetY + offsetY * alpha;
                    } else {
                        y = offsetY - offsetY * alpha;
                    }
                    break;
            }
            engine.batch.draw(next, x, y, 0, 0, sliceWidth, h, 1, 1, 0, i * sliceWidth, 0, sliceWidth,
                    next.getHeight(), false, true);
        }
        engine.batch.end();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


}

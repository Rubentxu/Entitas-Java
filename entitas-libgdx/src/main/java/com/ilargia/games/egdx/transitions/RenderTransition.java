package com.ilargia.games.egdx.transitions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.ilargia.games.egdx.base.BaseTransition;


public abstract class RenderTransition extends BaseTransition {

    protected final Batch batch;
    private FrameBuffer currFbo;
    private FrameBuffer nextFbo;
    Texture current, next;

    public RenderTransition(float duration, Batch batch) {
        super(duration);
        this.batch = batch;
    }

    @Override
    public void loadResources() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        nextFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, true);
        currFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, true);
    }

    @Override
    public void init() {
        currFbo.begin();
        oldState.render();
        currFbo.end();
        oldState.onPause();
        oldState.dispose();

        nextFbo.begin();
        newState.update(0);
        newState.render();
        nextFbo.end();

        current = currFbo.getColorBufferTexture();
        next = nextFbo.getColorBufferTexture();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void dispose() {
        currFbo = null;
        nextFbo = null;
        current = null;
        next = null;
    }

    @Override
    public void unloadResources() {
        currFbo.dispose();
        nextFbo.dispose();
        current.dispose();
        next.dispose();
    }
}

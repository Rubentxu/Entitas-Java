package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Score;
import com.ilargia.games.components.TextureView;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.api.system.ISystem;


public class RendererSystem implements ISystem.IRenderSystem, ISetPool<Pool> {
    private final BitmapFont font;
    private Group<Entity> _group;
    private ShapeRenderer sr;
    private OrthographicCamera cam;
    private Group<Entity> _groupScore;
    private Batch batch;
    private Group<Entity> _groupTextureView;

    public RendererSystem(ShapeRenderer sr, OrthographicCamera cam, Batch batch, BitmapFont font) {
        this.sr = sr;
        this.cam = cam;
        this.batch = batch;
        this.font = font;
    }

    @Override
    public void setPool(Pool pool) {
        _group = pool.getGroup(CoreMatcher.View());
        _groupScore = pool.getGroup(CoreMatcher.Score());
        _groupTextureView = pool.getGroup(CoreMatcher.TextureView());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();

        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);

        for (Entity e : _group.getEntities()) {
            View view = e.getView();

            if (view.shape instanceof Rectangle) {
                Rectangle ret = (Rectangle) view.shape;
                sr.rect(ret.x, ret.y, ret.width, ret.height);
            } else {
                Circle circle = (Circle) view.shape;
                sr.circle(circle.x, circle.y, circle.radius);
            }

        }

        sr.end();

        batch.begin();
        for (Entity e : _groupScore.getEntities()) {
            Score score = e.getScore();
            font.draw(batch, score.text + " " + score.points, score.x, score.y);
        }
        for (Entity e : _groupTextureView.getEntities()) {
            TextureView textureView = e.getTextureView();
            float originX = textureView.width * 0.5f;
            float originY = textureView.height * 0.5f;

            batch.draw(textureView.texture, textureView.position.x, textureView.position.y,
                    0, 0, textureView.width, textureView.height, 1, 1, textureView.rotation);


        }
        batch.end();
    }

}

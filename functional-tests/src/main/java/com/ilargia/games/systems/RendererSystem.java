package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;


public class RendererSystem implements IExecuteSystem, ISetPool<Pool> {
    private Group<Entity> _group;
    private ShapeRenderer sr;
    private OrthographicCamera cam;

    public RendererSystem(ShapeRenderer sr, OrthographicCamera cam) {
        this.sr = sr;
        this.cam = cam;
    }

    @Override
    public void setPool(Pool pool) {
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.View()));
    }

    @Override
    public void execute() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();

        sr.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.WHITE);

        for (Entity e : _group.getEntities()) {
            View view = e.getView();

            if(view.shape instanceof Rectangle) {
                Rectangle ret = (Rectangle) view.shape;
                sr.rect(ret.x, ret.y, ret.width, ret.height);
            } else {
                Circle circle = (Circle) view.shape;
                sr.circle(circle.x, circle.y, circle.radius);
            }

        }
        sr.end();
    }

}

package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.ilargia.games.components.TextureView;
import com.ilargia.games.core.GameContext;
import com.ilargia.games.core.GameEntity;
import com.ilargia.games.core.GameMatcher;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.group.Group;


public class RendererSystem implements IRenderSystem {


    private OrthographicCamera cam;
    private Batch batch;
    private Group<GameEntity> _groupTextureView;

    public RendererSystem(GameContext context, OrthographicCamera cam, Batch batch) {
        this.cam = cam;
        this.batch = batch;
        _groupTextureView = context.getGroup(GameMatcher.TextureView());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();


        batch.begin();
        for (GameEntity e : _groupTextureView.getEntities()) {
            TextureView view = e.getTextureView();

            float originX = view.texture.getRegionWidth()* 0.5f;
            float originY = view.texture.getRegionHeight() * 0.5f;

            batch.draw(view.texture, view.body.getPosition().x - originX,view.body.getPosition().y - originY,
                    originX, originY, view.texture.getRegionWidth(), view.texture.getRegionHeight() , 1, 1, 0);

        }
        batch.end();
    }

}

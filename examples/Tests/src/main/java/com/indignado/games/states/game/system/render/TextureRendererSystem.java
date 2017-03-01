package com.indignado.games.states.game.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.group.Group;
import com.indignado.games.states.game.component.game.TextureView;
import com.indignado.games.states.game.gen.Entitas;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.game.GameMatcher;


public class TextureRendererSystem implements IInitializeSystem, IRenderSystem {

    private Entitas entitas;
    private OrthographicCamera cam;
    private Batch batch;
    private Group<GameEntity> groupTextureView;

    public TextureRendererSystem(Entitas entitas, Batch batch) {
        this.batch = batch;
        this.entitas = entitas;

    }

    @Override
    public void initialize() {
        this.cam = entitas.scene.getCamera().camera;
        this.groupTextureView = entitas.game.getGroup(GameMatcher.TextureView());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        for (GameEntity e : groupTextureView.getEntities()) {
            TextureView view = e.getTextureView();
            Body body = e.getRigidBody().body;

            batch.draw(view.texture, body.getPosition().x - view.bounds.extentsX, body.getPosition().y - view.bounds.extentsY,
                    body.getPosition().x, body.getPosition().y, view.bounds.extentsX * 2, view.bounds.extentsY * 2, 1, 1, 0);

        }
        batch.end();
    }


}

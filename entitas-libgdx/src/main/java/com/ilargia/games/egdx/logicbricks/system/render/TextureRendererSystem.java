package com.ilargia.games.egdx.logicbricks.system.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.egdx.logicbricks.component.game.TextureView;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.group.Group;


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
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        batch.begin();
        for (GameEntity e : groupTextureView.getEntities()) {
            TextureView view = e.getTextureView();
            Body body = e.getRigidBody().body;
            processTextureFlip(view);

            batch.draw(view.texture, body.getPosition().x - view.bounds.extentsX, body.getPosition().y - view.bounds.extentsY,
                    body.getPosition().x, body.getPosition().y, view.bounds.extentsX * 2, view.bounds.extentsY * 2, 1, 1, 0);

        }
        batch.end();
    }


    private void processTextureFlip(TextureView view) {
        if ((view.flipX && !view.texture.isFlipX()) || (!view.flipX && view.texture.isFlipX())) {
            float temp = view.texture.getU();
            view.texture.setU(view.texture.getU2());
            view.texture.setU2(temp);
        }


        if ((view.flipY && !view.texture.isFlipY()) || (!view.flipY && view.texture.isFlipY())) {
            float temp = view.texture.getV();
            view.texture.setV(view.texture.getV2());
            view.texture.setV2(temp);

        }
    }

}

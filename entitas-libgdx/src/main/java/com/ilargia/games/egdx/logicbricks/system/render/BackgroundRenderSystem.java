package com.ilargia.games.egdx.logicbricks.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.PreferencesManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.scene.ParallaxLayer;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneEntity;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneMatcher;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.group.Group;

public class BackgroundRenderSystem implements IInitializeSystem, IRenderSystem {
    private final SceneContext context;
    private final Engine engine;
    private final Group<SceneEntity> group;
    private Camera camera;
    private Batch batch;
    private Vector2 speed = new Vector2();

    public BackgroundRenderSystem(Entitas entitas, Engine engine) {
        this.engine = engine;
        this.context = entitas.scene;
        this.group = context.getGroup(SceneMatcher.ParallaxLayer());
    }

    @Override
    public void initialize() {
        camera = engine.getManager(SceneManagerGDX.class).getDefaultCamera();
        batch = engine.getManager(SceneManagerGDX.class).getBatch();

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
       // this.camera.position.add(speed.x * delta, speed.y * delta, 0);
        for (SceneEntity entity : group.getEntities()) {
            ParallaxLayer layer = entity.getParallaxLayer();

            batch.setProjectionMatrix(camera.projection);
            batch.begin();

            float textureWidth = layer.background.getRegionWidth()/64;
            float textureHeight = layer.background.getRegionHeight()/64;
            float currentX = -camera.position.x * layer.parallaxRatio.x % (textureWidth + layer.padding.x);

            if (speed.x < 0) currentX += -(textureWidth + layer.padding.x);
            do {
                float currentY = -camera.position.y * layer.parallaxRatio.y % (textureHeight + layer.padding.y);
                if (speed.y < 0) currentY += -(textureHeight + layer.padding.y);
                do {

                    batch.draw(layer.background,-this.camera.viewportWidth / 2 + currentX + layer.startPosition.x,
                            -this.camera.viewportHeight / 2 + currentY + layer.startPosition.y,
                            textureWidth/2, textureHeight/2, textureWidth, textureHeight, 1, 1, 0);


                    currentY += (textureHeight + layer.padding.y);
                } while (currentY < camera.viewportHeight);
                currentX += (textureWidth + layer.padding.x);
            } while (currentX < camera.viewportWidth);

            batch.end();


        }
    }

    public void render(ParallaxLayer layer, float xPosition, float yPosition, float width, float height) {
        batch.draw(layer.background, xPosition, yPosition, width, height);

    }

}

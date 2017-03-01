package com.ilargia.games.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.Pong;
import com.ilargia.games.PongEngine;
import com.ilargia.games.core.Entitas;
import com.ilargia.games.egdx.base.BaseGameState;
import com.ilargia.games.egdx.base.managers.*;
import com.ilargia.games.systems.DelaySystem;
import com.ilargia.games.systems.RendererSystem;


public class SplashState extends BaseGameState {
    private String splash = "assets/textures/pong.jpg";
    private BaseAssetsManager assetsManager;
    private PongEngine engine;

    private Entitas context;

    public SplashState(PongEngine engine) {
        context = new Entitas();
        this.engine = engine;

    }

    @Override
    public void loadResources() {
        assetsManager = engine.getManager(BaseAssetsManager.class);
        assetsManager.loadTexture(splash);
        assetsManager.finishLoading();

    }

    @Override
    public void initialize() {
        // Input
        Camera camera = engine.getManager(BaseSceneManager.class).getDefaultCamera();
        Batch batch = engine.getManager(BaseSceneManager.class).getBatch();
        BitmapFont font = engine.getManager(BaseGUIManager.class).getDefaultFont();
        systems.add(new DelaySystem(context.core))
                .add(new RendererSystem(context.core, engine.sr, camera, batch, font));

        Texture texture = assetsManager.getTexture(splash);

        context.core.createEntity()
                .addTextureView("Pong", new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight()), new Vector2(),
                        0, Pong.SCREEN_HEIGHT, Pong.SCREEN_WIDTH)
                .addDelay(3);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


    @Override
    public void unloadResources() {
        assetsManager.unloadTexture(splash);
        context.core.destroyAllEntities();
        systems.clearSystems();
    }
}

package com.ilargia.games.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.Pong;
import com.ilargia.games.PongEngine;
import com.ilargia.games.core.Context;
import com.ilargia.games.egdx.interfaces.GameState;
import com.ilargia.games.egdx.managers.EGAssetsManager;
import com.ilargia.games.systems.*;


public class SplashState implements GameState<PongEngine> {
    private String splash = "assets/textures/pong.jpg";
    private EGAssetsManager assetsManager;


    @Override
    public void loadResources(PongEngine engine) {
        assetsManager = engine.getManager(EGAssetsManager.class);
        assetsManager.loadAsset(splash,Texture.class);
        assetsManager.finishLoading();

    }

    @Override
    public void init(PongEngine engine) {
        Context context = engine.context;
        engine._systems
                .addSystem(context.core, new DelaySystem())
                .addSystem(context.core, new RendererSystem(engine.sr, engine.cam, engine.batch, engine.font));

        Texture texture = assetsManager.getTexture(splash);

        context.core.createEntity()
                .addTextureView("Pong", new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight()), new Vector2(),
                       0, Pong.SCREEN_HEIGHT, Pong.SCREEN_WIDTH )
                .addDelay(3);
    }

    @Override
    public void onResume(PongEngine engine) {

    }

    @Override
    public void onPause(PongEngine engine) {

    }

    @Override
    public void unloadResources(PongEngine engine) {
        assetsManager.unloadAsset(splash);
        engine.context.core.destroyAllEntities();
        engine._systems.clearSystems();
    }
}

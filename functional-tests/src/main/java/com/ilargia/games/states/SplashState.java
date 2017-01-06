package com.ilargia.games.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.Pong;
import com.ilargia.games.PongEngine;
import com.ilargia.games.core.Context;
import com.ilargia.games.egdx.base.BaseGameState;
import com.ilargia.games.egdx.base.interfaces.GameState;
import com.ilargia.games.egdx.managers.EGAssetsManager;
import com.ilargia.games.entitas.Systems;
import com.ilargia.games.systems.*;


public class SplashState extends BaseGameState {
    private String splash = "assets/textures/pong.jpg";
    private EGAssetsManager assetsManager;
    private PongEngine engine;

    private Context context;

    public SplashState(PongEngine engine) {
        this.engine = engine;
        context = new Context();
    }

    @Override
    public void loadResources() {
        assetsManager = engine.getManager(EGAssetsManager.class);
        assetsManager.loadAsset(splash,Texture.class);
        assetsManager.finishLoading();

    }

    @Override
    public void initialize() {
        systems.addSystem(context.core, new DelaySystem())
                .addSystem(context.core, new RendererSystem(engine.sr, engine.cam, engine.batch, engine.font));

        Texture texture = assetsManager.getTexture(splash);

        context.core.createEntity()
                .addTextureView("Pong", new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight()), new Vector2(),
                        0, Pong.SCREEN_HEIGHT, Pong.SCREEN_WIDTH )
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
        assetsManager.unloadAsset(splash);
        context.core.destroyAllEntities();
        systems.clearSystems();
    }
}

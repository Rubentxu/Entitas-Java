package com.ilargia.games.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.MatchOne;
import com.ilargia.games.MatchOneEngine;
import com.ilargia.games.egdx.base.BaseGameState;
import com.ilargia.games.egdx.managers.EGAssetsManager;
import com.ilargia.games.systems.DelaySystem;
import com.ilargia.games.systems.RendererSystem;


public class SplashState extends BaseGameState {
    private String splash = "assets/textures/pong.jpg";
    private EGAssetsManager assetsManager;
    private MatchOneEngine engine;

    private Entitas entitas;

    public SplashState(MatchOneEngine engine) {
        entitas = new Entitas();
        this.engine = engine;

    }

    @Override
    public void loadResources() {
        assetsManager = engine.getManager(EGAssetsManager.class);
        assetsManager.loadTexture(splash);
        assetsManager.finishLoading();

    }

    @Override
    public void initialize() {
        systems.add(new DelaySystem(entitas.core))
                .add(new RendererSystem(entitas.core, engine.sr, engine.cam, engine.batch, engine.font));

        Texture texture = assetsManager.getTexture(splash);

        entitas.render.createEntity()
                .addTextureView("MatchOne", new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight()), new Vector2(),
                        0, MatchOne.SCREEN_HEIGHT, MatchOne.SCREEN_WIDTH)
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
        entitas.core.destroyAllEntities();
        systems.clearSystems();
    }
}

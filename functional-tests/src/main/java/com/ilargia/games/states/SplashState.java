package com.ilargia.games.states;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.Pong;
import com.ilargia.games.PongEngine;
import com.ilargia.games.components.Player;
import com.ilargia.games.core.Context;
import com.ilargia.games.egdx.interfaces.GameState;
import com.ilargia.games.egdx.managers.EGTextureManager;
import com.ilargia.games.systems.*;


public class SplashState implements GameState<PongEngine> {
    private String splash = "assets/textures/pong.jpg";
    private EGTextureManager textureManager;


    @Override
    public void loadResources(PongEngine engine) {
        textureManager = engine.getManager(EGTextureManager.class);
        textureManager.loadAsset(splash,Texture.class);

    }

    @Override
    public void init(PongEngine engine) {
        Context context = engine.context;
        engine._systems.addSystem(context.core, new RendererSystem(engine.sr, engine.cam, engine.batch, engine.font));
    }

    @Override
    public void onResume(PongEngine engine) {

    }

    @Override
    public void onPause(PongEngine engine) {

    }

    @Override
    public void unloadResources(PongEngine engine) {
        textureManager.unloadAsset(splash);
        engine.context.core.destroyAllEntities();
        engine._systems.clearSystems();
    }
}

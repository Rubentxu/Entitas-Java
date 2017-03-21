package com.examples.games.scenes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.factories.SceneFactory;
import com.ilargia.games.egdx.impl.managers.AssetsManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;


public class PlatformExamples implements SceneFactory<Engine, Entitas> {

    @Override
    public void createScene(Engine engine, Entitas entitas) {
        AssetsManagerGDX assetsManager = engine.getManager(AssetsManagerGDX.class);
        assetsManager.loadTexture("assets/imagenes/fondos/fondo.jpg");
        assetsManager.loadTexture("assets/imagenes/fondos/nubes.png");
        assetsManager.loadTexture("assets/imagenes/fondos/arboles.png");
        assetsManager.finishLoading();


        entitas.scene.createEntity()
                .addParallaxLayer(new TextureRegion(assetsManager.getTexture("assets/imagenes/fondos/fondo.jpg"))
                        , new Vector2(0.29f,0f),new Vector2(0, 0),new Vector2(0, 0));
        entitas.scene.createEntity()
                .addParallaxLayer(new TextureRegion(assetsManager.getTexture("assets/imagenes/fondos/nubes.png"))
                        , new Vector2(0.09f,1.0f),new Vector2(0, 0),new Vector2(0, 0));
        entitas.scene.createEntity()
                .addParallaxLayer(new TextureRegion(assetsManager.getTexture("assets/imagenes/fondos/arboles.png"))
                        , new Vector2(0.15f,0),new Vector2(0, 0),new Vector2(0, 20));


        SceneManagerGDX sceneManager = engine.getManager(SceneManagerGDX.class);
        GameEntity ground = sceneManager.createEntity("Ground");
        ground.getRigidBody().body.setTransform(10, 1, 0);

        GameEntity mariano = sceneManager.createEntity("Mariano");
        mariano.getRigidBody().body.setTransform(10, 6, 0);
    }
}

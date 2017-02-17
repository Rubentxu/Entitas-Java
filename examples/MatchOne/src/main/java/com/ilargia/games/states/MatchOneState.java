package com.ilargia.games.states;

import com.ilargia.games.EntityIndexExtension;
import com.ilargia.games.MatchOneEngine;
import com.ilargia.games.core.Entitas;

import com.ilargia.games.egdx.base.BaseGameState;
import com.ilargia.games.egdx.managers.EGAssetsManager;
import com.ilargia.games.systems.*;

public class MatchOneState extends BaseGameState {
    private String Blocker = "assets/textures/Blocker.png";
    private String Piece0 = "assets/textures/Piece0.png";
    private String Piece1 = "assets/textures/Piece1.png";
    private String Piece2 = "assets/textures/Piece2.png";
    private String Piece3 = "assets/textures/Piece3.png";
    private String Piece4 = "assets/textures/Piece4.png";
    private String Piece5 = "assets/textures/Piece5.png";


    private MatchOneEngine engine;
    private Entitas entitas;

    private EGAssetsManager assetsManager;


    public MatchOneState(MatchOneEngine engine) {
        this.engine = engine;


    }

    @Override
    public void loadResources() {
        assetsManager = engine.getManager(EGAssetsManager.class);
        assetsManager.loadTexture(Blocker);
        assetsManager.loadTexture(Piece0);
        assetsManager.loadTexture(Piece1);
        assetsManager.loadTexture(Piece2);
        assetsManager.loadTexture(Piece3);
        assetsManager.loadTexture(Piece4);
        assetsManager.loadTexture(Piece5);
        assetsManager.finishLoading();
    }

    @Override
    public void initialize() {
        entitas = new Entitas();
        EntityIndexExtension.addEntityIndices(entitas);
        // Input
        EmitInputSystem emitInputSystem = new EmitInputSystem(entitas.input, engine.physic, engine.cam);
        systems
                .add(new ProcessInputSystem(entitas))
                // Update
                .add(new GameBoardSystem(entitas.game))
                .add(new FallSystem(entitas.game))
                .add(new FillSystem(entitas.game))
                .add(new ScoreSystem(entitas))
                // Render
                .add(new RemoveViewSystem(entitas.game, engine.physic))
                .add(new AddViewSystem(entitas.game, assetsManager, engine.bodyBuilder))
                .add(new AnimatePositionSystem(entitas.game))
                // Destroy
                .add(new DestroySystem(entitas.game))
                .add(new RendererSystem(entitas, engine.cam, engine.batch, engine.physic))
        ;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {
        entitas.game.destroyAllEntities();
        systems.clearSystems();
    }


}

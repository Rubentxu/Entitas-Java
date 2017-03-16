package com.examples.games.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.examples.games.ExamplesEngine;
import com.ilargia.games.egdx.impl.GameStateGDX;
import com.ilargia.games.egdx.impl.managers.GUIManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.logicbrick.gen.Entitas;


public class PlatformExampleState extends GameStateGDX {

    private final ExamplesEngine engine;
    private final Entitas context;

    public PlatformExampleState(ExamplesEngine engine) {
        this.engine = engine;
        context = new Entitas();
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void initialize() {
        // Input
        Camera camera = engine.getManager(SceneManagerGDX.class).getDefaultCamera();
        Batch batch = engine.getManager(SceneManagerGDX.class).getBatch();
        BitmapFont font = engine.getManager(GUIManagerGDX.class).getDefaultFont();
//        context.core.createEntity()
//                .addBall(false)
//                .addView(new Circle(0, 0, 8))
//                .addMotion(MathUtils.clamp(1, 230, 300), 300);
//
//        context.core.createEntity()
//                .addPlayer(Player.ID.PLAYER1)
//                .addScore("Player 1: ", 180, 470)
//                .addView(new Rectangle(-350, 0, Pong.PLAYER_WIDTH, Pong.PLAYER_HEIGHT))
//                .addMotion(0, 0);
//
//        context.core.createEntity()
//                .addPlayer(Player.ID.PLAYER2)
//                .addScore("Player 2: ", 480, 470)
//                .addView(new Rectangle(350, 0, Pong.PLAYER_WIDTH, Pong.PLAYER_HEIGHT))
//                .addMotion(0, 0);
//
//        systems.add(new InputSystem(context.core))
//                .add(new ContactSystem(context.core))
//                .add(new BoundsSystem(context.core))
//                .add(new MoveSystem(context.core))
//                .add(new RendererSystem(context.core, engine.sr, camera, batch, font));
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {
        //context.core.destroyAllEntities();
        systems.clearSystems();
    }


}

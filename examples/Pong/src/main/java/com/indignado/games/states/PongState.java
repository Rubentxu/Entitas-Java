package com.indignado.games.states;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.Pong;
import com.ilargia.games.entitas.PongEngine;
import com.ilargia.games.logicbrick.component.Player;
import com.ilargia.games.entitas.core.Entitas;
import com.ilargia.games.entitas.egdx.base.BaseGameState;
import com.ilargia.games.entitas.egdx.base.managers.BaseGUIManager;
import com.ilargia.games.entitas.egdx.base.managers.BaseSceneManager;
import com.ilargia.games.entitas.systems.*;


public class PongState extends BaseGameState {

    private final PongEngine engine;
    private final Entitas context;

    public PongState(PongEngine engine) {
        this.engine = engine;
        context = new Entitas();
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void initialize() {
        // Input
        Camera camera = engine.getManager(BaseSceneManager.class).getDefaultCamera();
        Batch batch = engine.getManager(BaseSceneManager.class).getBatch();
        BitmapFont font = engine.getManager(BaseGUIManager.class).getDefaultFont();
        context.core.createEntity()
                .addBall(false)
                .addView(new Circle(0, 0, 8))
                .addMotion(MathUtils.clamp(1, 230, 300), 300);

        context.core.createEntity()
                .addPlayer(Player.ID.PLAYER1)
                .addScore("Player 1: ", 180, 470)
                .addView(new Rectangle(-350, 0, Pong.PLAYER_WIDTH, Pong.PLAYER_HEIGHT))
                .addMotion(0, 0);

        context.core.createEntity()
                .addPlayer(Player.ID.PLAYER2)
                .addScore("Player 2: ", 480, 470)
                .addView(new Rectangle(350, 0, Pong.PLAYER_WIDTH, Pong.PLAYER_HEIGHT))
                .addMotion(0, 0);

        systems.add(new InputSystem(context.core))
                .add(new ContactSystem(context.core))
                .add(new BoundsSystem(context.core))
                .add(new MoveSystem(context.core))
                .add(new RendererSystem(context.core, engine.sr, camera, batch, font));
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {
        context.core.destroyAllEntities();
        systems.clearSystems();
    }


}

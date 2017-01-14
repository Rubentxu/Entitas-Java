package com.ilargia.games.states;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.Pong;
import com.ilargia.games.PongEngine;
import com.ilargia.games.components.Player;
import com.ilargia.games.core.Context;
import com.ilargia.games.egdx.base.BaseGameState;
import com.ilargia.games.systems.*;


public class PongState extends BaseGameState {

    private final PongEngine engine;
    private final Context context;

    public PongState(PongEngine engine) {
        this.engine = engine;
        context = new Context();
    }

    @Override

    public void loadResources() {

    }

    @Override
    public void init() {
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

        systems.addSystem(context.core, new InputSystem())
                .addSystem(context.core, new ContactSystem())
                .addSystem(context.core, new BoundsSystem())
                .addSystem(context.core, new MoveSystem())
                .addSystem(context.core, new RendererSystem(engine.sr, engine.cam, engine.batch, engine.font));
    }

    @Override
    public void initialize() {

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

package com.ilargia.games;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Player;
import com.ilargia.games.core.Context;
import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.GameState;
import com.ilargia.games.systems.*;


public class PongState implements GameState<PongEngine> {

    @Override
    public void loadResources(PongEngine engine) {

    }

    @Override
    public void init(PongEngine engine) {
        Context context = engine.context;
        context.core.createEntity()
                .addBall(false)
                .addView(new Circle(0,0,8))
                .addMotion(MathUtils.clamp(1,230,300),300);

        context.core.createEntity()
                .addPlayer(Player.ID.PLAYER1)
                .addScore("Player 1: ", 180, 470 )
                .addView(new Rectangle(-350,0,Pong.PLAYER_WIDTH,Pong.PLAYER_HEIGHT))
                .addMotion(0,0);

        context.core.createEntity()
                .addPlayer(Player.ID.PLAYER2)
                .addScore("Player 2: ", 480, 470 )
                .addView(new Rectangle(350,0,Pong.PLAYER_WIDTH,Pong.PLAYER_HEIGHT))
                .addMotion(0,0);

        engine._systems.addSystem(context.core, new InputSystem())
                .addSystem(context.core, new ContactSystem())
                .addSystem(context.core, new BoundsSystem())
                .addSystem(context.core, new MoveSystem())
                .addSystem(context.core, new RendererSystem(engine.sr, engine.cam, engine.batch, engine.font));
    }

    @Override
    public void onResume(PongEngine engine) {

    }

    @Override
    public void onPause(PongEngine engine) {

    }

    @Override
    public void unloadResources(PongEngine engine) {

    }
}

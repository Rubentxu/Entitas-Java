package com.ilargia.games.states;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.MatchOneEngine;
import com.ilargia.games.Pong;
import com.ilargia.games.PongEngine;
import com.ilargia.games.components.Player;
import com.ilargia.games.egdx.base.BaseGameState;
import com.ilargia.games.systems.*;


public class MatchOneState extends BaseGameState {

    private final MatchOneEngine engine;
    private final Entitas context;

    public MatchOneState(MatchOneEngine engine) {
        this.engine = engine;
        context = new Entitas();
    }

    @Override
    public void loadResources() {

    }

    @Override
    public void initialize() {
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
                .add( new ContactSystem(context.core))
                .add( new BoundsSystem(context.core))
                .add( new MoveSystem(context.core))
                .add( new RendererSystem(context.core, engine.sr, engine.cam, engine.batch, engine.font));
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

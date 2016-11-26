package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.*;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;


public class BoundsSystem implements IExecuteSystem, ISetPool<Pool> {
    public static int WIDTH = Gdx.graphics.getWidth();
    private Group<Entity> _groupPlayer;
    private Pool _pool;

    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _groupPlayer = pool.getGroup(Matcher.AllOf(CoreMatcher.Player()));

    }

    @Override
    public void execute() {
        Entity ball =  _pool.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;

        for (Entity e : _groupPlayer.getEntities()) {
            Player player = e.getPlayer();

            if (ballShape.x + ballShape.radius <= -(WIDTH/2) && player.id == Player.ID.PLAYER1)
                player.score+=10;
            if (ballShape.x - ballShape.radius >= (WIDTH/2) && player.id == Player.ID.PLAYER2)
                player.score+=10;

        }

    }

}



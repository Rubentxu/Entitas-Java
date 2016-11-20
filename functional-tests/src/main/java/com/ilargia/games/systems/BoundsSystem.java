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
    public static int HEIGHT = Gdx.graphics.getHeight();
    public static int WIDTH = Gdx.graphics.getWidth();
    private Group<Entity> _groupBounds;
    private Group<Entity> _groupScore;
    private Pool _pool;


    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _groupBounds = pool.getGroup(Matcher.AllOf(CoreMatcher.Bounds()));
        _groupScore = pool.getGroup(Matcher.AllOf(CoreMatcher.Score(), CoreMatcher.Player()));

    }

    @Override
    public void execute() {
        boolean scorePlayer1 = false;
        boolean scorePlayer2 = false;

        Entity ball = _pool.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;

        for (Entity e : _groupBounds.getEntities()) {
            Bounds bounds = e.getBounds();

            if (collidesCircleRectangle(ballShape, bounds.rectangle)) {
                resetBoard(ballShape);
                if(bounds.tag == Bounds.Tag.BoundPlayer1) scorePlayer2 = true;
                if(bounds.tag == Bounds.Tag.BoundPlayer2) scorePlayer1 = true;
            }

        }

        if(scorePlayer1 || scorePlayer2) {
            for (Entity e : _groupScore.getEntities()) {
                Score score = e.getScore();
                Player player = e.getPlayer();

                if (scorePlayer1 && player.id == Player.ID.PLAYER1) {
                    score.value+= 10;
                }

                if (scorePlayer2 && player.id == Player.ID.PLAYER2) {
                    score.value+= 10;
                }

            }
        }


    }

    private void resetBoard(Circle pong) {
        pong.x = WIDTH / 2;
        pong.y = HEIGHT / 3;

    }

    public boolean collidesCircleRectangle(Circle circA, Rectangle rectA) {
        float circleDistanceX = Math.abs(circA.x - rectA.x - rectA.width / 2);
        float circleDistanceY = Math.abs(circA.y - rectA.y - rectA.height / 2);

        if (circleDistanceX > (rectA.width / 2 + circA.radius) || circleDistanceY > (rectA.height / 2 + circA.radius))
            return false;
        if (circleDistanceX <= (rectA.width / 2) || circleDistanceX <= (rectA.height / 2))
            return true;

        float cornerDistance = (circleDistanceX - rectA.width / 2) * (circleDistanceX - rectA.width / 2)
                + (circleDistanceY - rectA.height / 2) * (circleDistanceY - rectA.height / 2);

        return (cornerDistance <= (circA.radius * circA.radius));
    }


}



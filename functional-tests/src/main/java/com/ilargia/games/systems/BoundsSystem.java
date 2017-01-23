package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.Player;
import com.ilargia.games.components.Score;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.matcher.Matcher;


public class BoundsSystem implements IExecuteSystem, ISetPool<Pool> {
    public static int WIDTH = Gdx.graphics.getWidth();
    private Group<Entity> _groupPlayer;
    private Pool _pool;

    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _groupPlayer = pool.getGroup(Matcher.AllOf(CoreMatcher.Player(), CoreMatcher.Score()));

    }

    @Override
    public void execute(float deltatime) {
        Entity ball = _pool.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;
        Motion motion = ball.getMotion();

        for (Entity e : _groupPlayer.getEntities()) {
            Player player = e.getPlayer();
            Score score = e.getScore();

            if (ballShape.x + ballShape.radius <= -(WIDTH / 2) && player.id == Player.ID.PLAYER2)
                restart(ballShape, motion, score);

            if (ballShape.x - ballShape.radius >= (WIDTH / 2) && player.id == Player.ID.PLAYER1)
                restart(ballShape, motion, score);

        }

    }

    private void restart(Circle ballShape, Motion ballMotion, Score score) {
        score.points += 10;
        ballShape.setPosition(0, 0);
        ballMotion.velocity.set(MathUtils.clamp(1, 230, 300), 300);
        ballMotion.velocity.x *= -1;
    }

}



package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.Player;
import com.ilargia.games.components.Score;
import com.ilargia.games.core.CoreContext;
import com.ilargia.games.core.CoreEntity;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.matcher.Matcher;


public class BoundsSystem implements IExecuteSystem {
    public static int WIDTH = Gdx.graphics.getWidth();
    private Group<CoreEntity> _groupPlayer;
    private CoreContext _context;


    public BoundsSystem(CoreContext context) {
        _context = context;
        _groupPlayer = context.getGroup(Matcher.AllOf(CoreMatcher.Player(), CoreMatcher.Score()));

    }

    @Override
    public void execute(float deltatime) {
        CoreEntity ball = _context.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;
        Motion motion = ball.getMotion();

        for (CoreEntity e : _groupPlayer.getEntities()) {
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



package com.ilargia.games.systems;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.Pong;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.interfaces.ISystem;
import com.ilargia.games.entitas.matcher.Matcher;


public class ContactSystem implements ISystem.IExecuteSystem, ISetPool<Pool> {

    int pongSpeed = 300;
    private Group<Entity> _group;
    private Pool _pool;
    private Rectangle temp;

    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.View(), CoreMatcher.Motion(), CoreMatcher.Player()));
        temp = new Rectangle();
    }

    @Override
    public void execute(float deltatime) {
        Entity ball = _pool.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;
        Motion ballMotion = ball.getMotion();

        if (ballShape.y - ballShape.radius <= -(Pong.SCREEN_HEIGHT / 2)) {
            ballShape.setY(-(Pong.SCREEN_HEIGHT / 2) + ballShape.radius);
            ballMotion.velocity.y = -(ballMotion.velocity.y + 10);
            ballMotion.velocity.x = ballMotion.velocity.x + 10;
        }

        if (ballShape.y + ballShape.radius >= (Pong.SCREEN_HEIGHT / 2)) {
            ballShape.setY((Pong.SCREEN_HEIGHT / 2) - ballShape.radius);
            ballMotion.velocity.y = -(ballMotion.velocity.y + 10);
            ballMotion.velocity.x = ballMotion.velocity.x + 10;
        }


        for (Entity e : _group.getEntities()) {
            View view = e.getView();
            circleRectCollision(ballShape, (Rectangle) view.shape, ballMotion);
        }

    }

    public void circleRectCollision(Circle circle, Rectangle rectangle, Motion ballMotion) {
        temp.set(circle.x - circle.radius, circle.y + circle.radius, circle.radius * 2, circle.radius * 2);

        if (temp.overlaps(rectangle)) {
            if (ballMotion.velocity.x <= 0) circle.setX(rectangle.x + rectangle.width + circle.radius);
            if (ballMotion.velocity.x >= 0) circle.setX(rectangle.x - rectangle.width + circle.radius);
            ballMotion.velocity.x = (float) -(ballMotion.velocity.x * 1.05);
            ballMotion.velocity.y = (float) (ballMotion.velocity.y * 1.05);

        }

    }


}



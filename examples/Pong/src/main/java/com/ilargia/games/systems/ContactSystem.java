package com.ilargia.games.systems;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.Pong;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreContext;
import com.ilargia.games.core.CoreEntity;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;


public class ContactSystem implements IExecuteSystem {

    int pongSpeed = 300;
    private Group<CoreEntity> _group;
    private CoreContext _context;
    private Rectangle temp;


    public ContactSystem(CoreContext context) {
        _context = context;
        _group = context.getGroup(Matcher.AllOf(CoreMatcher.View(), CoreMatcher.Motion(), CoreMatcher.Player()));
        temp = new Rectangle();
    }

    @Override
    public void execute(float deltatime) {
        CoreEntity ball = _context.getBallEntity();
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


        for (CoreEntity e : _group.getEntities()) {
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



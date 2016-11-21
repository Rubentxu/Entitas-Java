package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Ball;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;


public class ContactSystem implements IExecuteSystem, ISetPool<Pool> {

    public interface FactoryComponent {}

    public interface FactoryMotion extends FactoryComponent{
        Motion getMotion(float y, float x);
    }

    public interface FactoryBall extends FactoryComponent {
        Ball getBall(boolean resetBall);
    }


    public static int HEIGHT = Gdx.graphics.getHeight();
    private Group<Entity> _group;
    private Pool _pool;
    int pongSpeed = 300;

    @Override
    public void setPool(Pool pool) {
        FactoryMotion func = Motion::new;
        FactoryBall fun2 = Ball::new;
        FactoryComponent[] factorias = new FactoryComponent[2];
        factorias[0] =func;
        factorias[1]=fun2;

        FactoryMotion re= (FactoryMotion) factorias[0];
        re.getMotion(2,2);


        _pool = pool;
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.View(), CoreMatcher.Motion()));
    }

    @Override
    public void execute() {
        Entity ball =  _pool.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;
        Motion ballMotion = ball.getMotion();
        for (Entity e : _group.getEntities()) {
            Motion motion = e.getMotion();
            View view = e.getView();

            if (ball.getView() != view) {
                if (collidesCircleRectangle(ballShape, (Rectangle) view.shape)
                        || ballMotion.velocity.y > (HEIGHT / 2) || ballMotion.velocity.y < (HEIGHT / 2)) {
                    if (motion.velocity.x > 0) ballMotion.velocity.x = pongSpeed * Gdx.graphics.getDeltaTime();
                    if (motion.velocity.x < 0) ballMotion.velocity.x = -pongSpeed * Gdx.graphics.getDeltaTime();
                    if (motion.velocity.y > 0) ballMotion.velocity.y = pongSpeed * Gdx.graphics.getDeltaTime();
                    if (motion.velocity.y < 0) ballMotion.velocity.y = -pongSpeed * Gdx.graphics.getDeltaTime();
                }
            }


        }

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



package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.components.Ball;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.Player;
import com.ilargia.games.components.View;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;



public class ContactSystem implements IExecuteSystem, ISetPool<Pool> {

    public static int HEIGHT = Gdx.graphics.getHeight();
    private Group<Entity> _group;
    private Pool _pool;
    int pongSpeed = 300;

    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.View(), CoreMatcher.Motion(), CoreMatcher.Player()));
    }

    @Override
    public void execute() {
        Entity ball =  _pool.getBallEntity();
        Circle ballShape = (Circle) ball.getView().shape;
        Motion ballMotion = ball.getMotion();

        if (ballShape.y - ballShape.radius <= -(HEIGHT/2))
            ballMotion.velocity.y = pongSpeed ;
        if (ballShape.y + ballShape.radius >= (HEIGHT/2))
            ballMotion.velocity.y = -(pongSpeed);


        for (Entity e : _group.getEntities()) {
            Motion motion = e.getMotion();
            View view = e.getView();
            Player player = e.getPlayer();

            if (player.id == Player.ID.PLAYER1 && checkBoundsPlayer(e, ballShape.x, ballShape.y)) {
                ballMotion.velocity.x = -ballMotion.velocity.x;
//                ballMotion.velocity.y = -ballMotion.velocity.y;
            }

            if (player.id == Player.ID.PLAYER2 && checkBoundsPlayer(e, ballShape.x, ballShape.y)) {
                ballMotion.velocity.x = -ballMotion.velocity.x;
//                ballMotion.velocity.y = -ballMotion.velocity.y;
            }





        }


    }

    private boolean checkBoundsPlayer(Entity e, float x, float y) {
        View view = e.getView();
        return view.shape.contains(x, y);

    }




}



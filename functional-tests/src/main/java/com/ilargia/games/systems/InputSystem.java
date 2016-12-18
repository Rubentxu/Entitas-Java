package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.Pong;
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


public class InputSystem implements IExecuteSystem, ISetPool<Pool> {
    private Group<Entity> _group;
    

    @Override
    public void setPool(Pool pool) {
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.Motion(), CoreMatcher.Player(), CoreMatcher.View()));
    }

    @Override
    public void execute(float deltatime) {
        for (Entity e : _group.getEntities()) {
            Motion motion = e.getMotion();
            Player player = e.getPlayer();
            View view = e.getView();

            if(player.id == Player.ID.PLAYER1) {
                motion.velocity.y = 0;
                if(movesUp(Input.Keys.W, (Rectangle) view.shape)) { motion.velocity.y = Pong.PLAYER_SPEED;  }
                if(movesDown(Input.Keys.S, (Rectangle) view.shape)) { motion.velocity.y = -Pong.PLAYER_SPEED;  }

            }

            if(player.id == Player.ID.PLAYER2) {
                motion.velocity.y = 0;
                if(movesUp(Input.Keys.UP, (Rectangle) view.shape)) { motion.velocity.y = Pong.PLAYER_SPEED; }
                if(movesDown(Input.Keys.DOWN, (Rectangle) view.shape)) { motion.velocity.y = -Pong.PLAYER_SPEED; }

            }

        }
    }

    private boolean movesUp(int key, Rectangle body) {
        return Gdx.input.isKeyPressed(key) && (body.y < Pong.SCREEN_HEIGHT/2 - Pong.PLAYER_HEIGHT);
    }

    private boolean movesDown(int key, Rectangle body) {
        return Gdx.input.isKeyPressed(key) && (body.y > -(Pong.SCREEN_HEIGHT/2 ));
    }

}

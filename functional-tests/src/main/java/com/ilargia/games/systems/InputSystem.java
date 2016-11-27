package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.Player;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.core.Pool;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;


public class InputSystem implements IExecuteSystem, ISetPool<Pool> {
    private Group<Entity> _group;
    private int playerSpeed = 200;

    @Override
    public void setPool(Pool pool) {
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher.Motion(), CoreMatcher.Player()));
    }

    @Override
    public void execute() {
        for (Entity e : _group.getEntities()) {
            Motion motion = e.getMotion();
            Player player = e.getPlayer();

            if(player.id == Player.ID.PLAYER1) {
                motion.velocity.y = 0;
                if(Gdx.input.isKeyPressed(Input.Keys.W)) { motion.velocity.y = playerSpeed;  }
                if(Gdx.input.isKeyPressed(Input.Keys.S)) { motion.velocity.y = -playerSpeed;  }

            }

            if(player.id == Player.ID.PLAYER2) {
                motion.velocity.y = 0;
                if(Gdx.input.isKeyPressed(Input.Keys.UP)) { motion.velocity.y = playerSpeed; }
                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) { motion.velocity.y = -playerSpeed; }

            }

        }
    }

}

package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ilargia.games.components.Motion;
import com.ilargia.games.components.Identity;
import com.ilargia.games.core.CoreMatcher;
import com.ilargia.games.core.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.Pool;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;


public class InputSystem implements IExecuteSystem, ISetPool {
    private Group<Entity> _group;
    private int playerSpeed = 125;

    @Override
    public void setPool(Pool pool) {
        _group = pool.getGroup(Matcher.AllOf(CoreMatcher._matcherMotion, CoreMatcher._matcherIdentity));
    }

    @Override
    public void execute() {
        for (Entity e : _group.getEntities()) {
            Motion motion = e.getMotion();
            Identity player = e.getIdentity();

            if(player.id == Identity.ID.PLAYER1) {
                if(Gdx.input.isKeyPressed(Input.Keys.W)) { motion.velocity.y = playerSpeed * Gdx.graphics.getDeltaTime(); }
                if(Gdx.input.isKeyPressed(Input.Keys.S)) { motion.velocity.y = -playerSpeed * Gdx.graphics.getDeltaTime(); }

            }

            if(player.id == Identity.ID.PLAYER2) {
                if(Gdx.input.isKeyPressed(Input.Keys.UP)) { motion.velocity.y = playerSpeed * Gdx.graphics.getDeltaTime(); }
                if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) { motion.velocity.y = -playerSpeed * Gdx.graphics.getDeltaTime(); }

            }

            if(player.id == Identity.ID.BALL) {
                
            }

        }
    }

}

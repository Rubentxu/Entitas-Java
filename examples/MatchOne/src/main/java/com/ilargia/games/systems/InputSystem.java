package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.ICleanupSystem;
import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;


public class InputSystem implements ISetPool<Pool>, IExecuteSystem, ICleanupSystem {
    private Pool _pool;
    private Group<Entity> _inputs;
    private boolean input;


    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _inputs = _pool.getGroup(InputMatcher.Input());
    }

    @Override
    public void execute() {
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            _pool.setBurstMode(!_pool.isBurstMode());
        }

        input = _pool.isBurstMode()
                ? Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                : (input == true) ? false : Gdx.input.isButtonPressed(Input.Buttons.LEFT);
/*
        if (input) {
            hit = Physics2D.Raycast(Camera.ScreenToWorldPoint(Input.mousePosition), Vector2.Zero, 100);
            if (hit.collider != null) {
                Position pos = hit.collider.transform.position;

                _pool.createEntity()
                        .addInput((int) pos.x, (int) pos.y);
            }
        }*/
    }

    @Override
    public void cleanup() {
        for (Entity e : _inputs.getEntities()) {
            _pool.destroyEntity(e);
        }
    }

}

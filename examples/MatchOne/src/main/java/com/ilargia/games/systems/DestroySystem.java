package com.ilargia.games.systems;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class DestroySystem implements ISetPool<Pool>, IReactiveSystem<Entity> {
    private Pool _pool;

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.Destroy().OnEntityAdded();
    }


    @Override
    public void setPool(Pool pool) {
        _pool = pool;
    }

    @Override
    public void execute(Array<Entity> entities) {

        for (Entity e : entities) {
            _pool.destroyEntity(e);
        }

    }


}

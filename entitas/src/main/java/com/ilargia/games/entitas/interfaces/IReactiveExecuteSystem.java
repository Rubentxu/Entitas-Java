package com.ilargia.games.entitas.interfaces;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.Entity;

public interface IReactiveExecuteSystem extends ISystem {
    void execute(Array<Entity> entities);
}
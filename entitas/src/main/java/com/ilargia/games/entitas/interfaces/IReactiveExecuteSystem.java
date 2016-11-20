package com.ilargia.games.entitas.interfaces;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.Entity;

public interface IReactiveExecuteSystem<E extends Entity> extends ISystem {
    void execute(Array<E> entities);
}
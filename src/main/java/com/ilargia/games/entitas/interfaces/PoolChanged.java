package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Pool;

@FunctionalInterface
public interface PoolChanged {
    void poolChanged(Pool pool, Entity entity);
}

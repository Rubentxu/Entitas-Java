package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Pool;

@FunctionalInterface
public interface PoolChanged<E extends Entity> {
    void poolChanged(Pool pool, E entity);
}

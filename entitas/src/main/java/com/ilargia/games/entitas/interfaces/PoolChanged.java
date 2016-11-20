package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.BasePool;

@FunctionalInterface
public interface PoolChanged<P extends BasePool, E extends Entity> {
    void poolChanged(P pool, E entity);
}

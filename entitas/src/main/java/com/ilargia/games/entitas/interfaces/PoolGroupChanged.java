package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.Pool;

@FunctionalInterface
public interface PoolGroupChanged {
    void groupChanged(Pool pool, Group group);
}

package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.BasePool;

@FunctionalInterface
public interface PoolGroupChanged<P extends BasePool> {
    void groupChanged(P pool, Group group);
}

package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.Group;

@FunctionalInterface
public interface PoolGroupChanged<P extends BasePool> {
    void groupChanged(final P pool, final Group group);
}

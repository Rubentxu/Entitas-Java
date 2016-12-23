package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.BasePool;

@FunctionalInterface
public interface PoolGroupChanged<P extends BasePool> {
    void groupChanged(final P pool, final Group group);
}

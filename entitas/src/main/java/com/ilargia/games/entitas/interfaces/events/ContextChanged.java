package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.BaseContext;
import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface ContextChanged<P extends BaseContext, E extends Entity> {
    void poolChanged(final P pool, final E entity);
}

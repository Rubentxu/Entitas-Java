package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface EntityReleased<E extends Entity> {
    void entityReleased(final E entity);
}

package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface EntityReleased<E extends Entity> {
    void entityReleased(E entity);
}

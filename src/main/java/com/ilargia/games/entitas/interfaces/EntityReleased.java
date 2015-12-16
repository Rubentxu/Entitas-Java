package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface EntityReleased {
    void entityReleased(Entity entity);
}

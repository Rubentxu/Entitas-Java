package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface EntityReleased {
    void released(final IEntity entity);
}

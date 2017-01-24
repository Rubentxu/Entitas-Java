package com.ilargia.games.entitas.api.events;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface EntityComponentChanged<TEntity extends IEntity> {
    void changed(final TEntity entity, final int index, final IComponent component);

}
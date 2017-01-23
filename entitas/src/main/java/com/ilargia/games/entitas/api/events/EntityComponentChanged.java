package com.ilargia.games.entitas.api.events;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;

@FunctionalInterface
public interface EntityComponentChanged {
    void changed(final IEntity entity, final int index, final IComponent component);

}
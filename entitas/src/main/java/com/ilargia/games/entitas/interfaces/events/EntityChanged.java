package com.ilargia.games.entitas.interfaces.events;


import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.interfaces.IComponent;

@FunctionalInterface
public interface EntityChanged<E extends Entity> {
    void entityChanged(final E entity, final int index, final IComponent component);

}
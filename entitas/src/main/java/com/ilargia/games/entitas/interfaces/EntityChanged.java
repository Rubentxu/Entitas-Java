package com.ilargia.games.entitas.interfaces;


import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface EntityChanged<E extends Entity> {
    void entityChanged(E entity, int index, IComponent component);

}
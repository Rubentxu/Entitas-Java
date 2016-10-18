package com.ilargia.games.entitas.interfaces;


import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface EntityChanged {
    void entityChanged(Entity entity, int index, IComponent component);

}
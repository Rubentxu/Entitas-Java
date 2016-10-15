package com.ilargia.games.entitas.interfaces;


import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Component;

@FunctionalInterface
public interface EntityChanged {
    void entityChanged(Entity entity, int index, Component component);

}
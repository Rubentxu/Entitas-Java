package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface ComponentReplaced<E extends Entity> {
    void componentReplaced(E entity, int index, IComponent previousComponent, IComponent newComponent);

}

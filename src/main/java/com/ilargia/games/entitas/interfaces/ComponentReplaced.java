package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

@FunctionalInterface
public interface ComponentReplaced {
    void componentReplaced(Entity entity, int index, IComponent previousComponent, IComponent newComponent);

}

package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.interfaces.IComponent;

@FunctionalInterface
public interface ComponentReplaced<E extends Entity> {
    void componentReplaced(final E entity, final int index, final IComponent previousComponent, final IComponent newComponent);

}

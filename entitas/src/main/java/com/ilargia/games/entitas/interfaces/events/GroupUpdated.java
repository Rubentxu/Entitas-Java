package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IComponent;

@FunctionalInterface
public interface GroupUpdated<E> {
    void groupUpdated(final Group group, final E entity, final int index, final IComponent previousComponent, final IComponent newComponent);

}
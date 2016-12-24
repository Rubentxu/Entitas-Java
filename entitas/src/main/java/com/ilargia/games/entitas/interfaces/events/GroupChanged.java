package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IComponent;

@FunctionalInterface
public interface GroupChanged<E extends Entity> {
    void groupChanged(final Group<E> group, final E entity, final int index, final IComponent component);

}

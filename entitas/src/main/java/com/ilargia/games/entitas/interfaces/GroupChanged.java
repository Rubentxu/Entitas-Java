package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.exceptions.EntityIndexException;

@FunctionalInterface
public interface GroupChanged<E extends Entity> {
    void groupChanged(Group<E> group, E entity, int index, IComponent component);

}

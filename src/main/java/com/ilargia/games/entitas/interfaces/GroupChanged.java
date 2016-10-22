package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.exceptions.EntityIndexException;

@FunctionalInterface
public interface GroupChanged {
    void groupChanged(Group group, Entity entity, int index, IComponent component) throws EntityIndexException;

}

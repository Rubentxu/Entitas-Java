package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;

@FunctionalInterface
public interface GroupChanged {
    void groupChanged(Group group, Entity entity, int index, IComponent component);

}

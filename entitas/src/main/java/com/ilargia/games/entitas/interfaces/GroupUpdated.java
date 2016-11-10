package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;

@FunctionalInterface
public interface GroupUpdated {
    void groupUpdated(Group group, Entity entity, int index, IComponent previousComponent, IComponent newComponent);

}
package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.Component;

@FunctionalInterface
public interface GroupUpdated {
    void groupUpdated(Group group, Entity entity, int index, Component previousComponent, Component newComponent);

}
package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Component;

@FunctionalInterface
public interface ComponentReplaced {
    void componentReplaced(Entity entity, int index, Component previousComponent, Component newComponent);

}

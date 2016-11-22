package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.EntityMetaData;

import java.util.Stack;

public interface FactoryEntity<E extends Entity> {
    E create(int totalComponents, Stack<IComponent>[] componentPools, EntityMetaData entityMetaData);
}
package com.ilargia.games.entitas.api;

import java.util.Stack;

public interface FactoryEntity<E extends IEntity> {
    E create(int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo);
}
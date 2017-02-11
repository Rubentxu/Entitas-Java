package com.ilargia.games.entitas.api;

import java.util.Stack;

@FunctionalInterface
public interface EntityBaseFactory<E extends IEntity> {
    E create(int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo);
}
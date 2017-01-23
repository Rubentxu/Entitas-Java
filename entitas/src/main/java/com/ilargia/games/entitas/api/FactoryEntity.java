package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.IComponent;

import java.util.Stack;

public interface FactoryEntity<E extends IEntity> {
    E create(int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo);
}
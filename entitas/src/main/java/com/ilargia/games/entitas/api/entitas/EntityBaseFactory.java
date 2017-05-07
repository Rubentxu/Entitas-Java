package com.ilargia.games.entitas.api.entitas;

@FunctionalInterface
public interface EntityBaseFactory<E extends IEntity> {
    E create();
}
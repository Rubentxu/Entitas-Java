package com.ilargia.games.entitas.interfaces;



@FunctionalInterface
public interface Actuator<C extends IComponent> {
    void modify(C component);

}

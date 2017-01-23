package com.ilargia.games.entitas.interfaces;


import com.ilargia.games.entitas.api.IComponent;

@FunctionalInterface
public interface Actuator<C extends IComponent> {
    void modify(C component);

}

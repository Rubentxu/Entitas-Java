package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.interfaces.IComponent;


public class Movable implements IComponent {
    public boolean isMovable = false;

    public Movable(boolean isMovable) {
        this.isMovable = isMovable;

    }

}
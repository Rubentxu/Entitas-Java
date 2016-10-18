package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.interfaces.IComponent;


public class Movable extends IComponent {
    private boolean isMovable = false;

    public Movable(boolean isMovable) {
        this.isMovable = isMovable;

    }

    public boolean isMovable() {
        return this.isMovable;
    }

    public void setMovable(boolean x) {
        this.isMovable = x;
    }


}
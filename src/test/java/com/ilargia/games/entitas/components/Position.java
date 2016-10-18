package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.interfaces.IComponent;

/**
 * Created by rdcabrera on 16/12/2015.
 */
public class Position extends IComponent {
    private float x, y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
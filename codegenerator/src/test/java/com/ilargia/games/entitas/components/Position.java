package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.interfaces.IComponent;

/**
 * Created by rdcabrera on 16/12/2015.
 */
public class Position implements IComponent {
    public float x, y;

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }


}
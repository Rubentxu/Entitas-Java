package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.api.IComponent;

@Component(pools = {"Test"})
public class Position implements IComponent {
    public float x, y;

    public Position() {
        this.x = 1;
        this.y = 1;
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

}
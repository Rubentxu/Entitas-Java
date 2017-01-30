package com.ilargia.games.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Input"})
public class Input implements IComponent {
    public float x;
    public float y;

    public Input(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

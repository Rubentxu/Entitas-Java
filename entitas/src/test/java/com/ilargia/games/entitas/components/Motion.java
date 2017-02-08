package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Test"})
public class Motion implements IComponent {
    public float x;
    public float y;

    public Motion(float x, float y) {
        this.x = x;
        this.y = y;
    }


}

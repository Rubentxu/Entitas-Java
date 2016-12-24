package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Test"})
public class Motion implements IComponent {
    public float x;
    public float y;

    public Motion(float x, float y) {
        this.x = x;
        this.y = y;
    }


}

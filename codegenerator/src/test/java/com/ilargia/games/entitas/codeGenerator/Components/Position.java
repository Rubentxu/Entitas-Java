package com.ilargia.games.entitas.codeGenerator.Components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;


@Component(pools = {"Core"})
public class Position implements IComponent {
    public float x;
    public float y;


    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }


}
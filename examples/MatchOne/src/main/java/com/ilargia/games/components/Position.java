package com.ilargia.games.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.api.IComponent;

@Component(pools = {"Game"})
public class Position implements IComponent {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

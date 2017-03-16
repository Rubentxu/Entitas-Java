package com.ilargia.games.egdx.logicbricks.component.input;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;

@Component(pools = {"Input"})
public class Input implements IComponent {
    public int x;
    public int y;

    public Input(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

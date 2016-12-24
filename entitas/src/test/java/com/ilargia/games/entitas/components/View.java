package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Test"})
public class View implements IComponent {
    public int shape;

    public View(int shape) {
        this.shape = shape;
    }
}
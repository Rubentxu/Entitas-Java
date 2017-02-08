package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Test"})
public class View implements IComponent {
    public int shape;

    public View(int shape) {
        this.shape = shape;
    }
}
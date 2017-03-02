package com.ilargia.games.core.component.gui;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Gui"})
public class Bounds implements IComponent {
    public float extentsX;
    public float extentsY;

    public Bounds(float extentsX, float extentsY) {
        this.extentsX = extentsX;
        this.extentsY = extentsY;
    }

}

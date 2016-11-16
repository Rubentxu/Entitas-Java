package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(
        pools = {"Core", "Ejemplo"}
)
public class Movable implements IComponent {
    public boolean isMovable = false;

    public Movable(boolean isMovable) {
        this.isMovable = isMovable;

    }

}
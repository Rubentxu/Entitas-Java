package com.ilargia.games.entitas.codeGenerator.Components;

import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class View implements IComponent {
    public Rectangle shape;

    public View(float x, float y, float width, float height) {
        this.shape = new Rectangle(x, y, width, height);
    }
}
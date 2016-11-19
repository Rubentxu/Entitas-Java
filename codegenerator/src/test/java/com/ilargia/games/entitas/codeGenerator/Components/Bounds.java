package com.ilargia.games.entitas.codeGenerator.Components;

import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class Bounds implements IComponent {
    public Rectangle rectangle;

    public Bounds(float x, float y, float width, float height) {
        this.rectangle = new Rectangle(x, y, width, height);
    }
}
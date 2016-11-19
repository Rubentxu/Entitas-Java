package com.ilargia.games.entitas.codeGenerator.Components;

import com.badlogic.gdx.math.Rectangle;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"}, isSingleEntity = true)
public class Screen implements IComponent {
    public float width;
    public float height;

    public Screen(float width, float height) {
        this.width = width;
        this.height = height;
    }
}

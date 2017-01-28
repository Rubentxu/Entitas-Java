package com.ilargia.games.components;

import com.badlogic.gdx.graphics.Texture;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class View implements IComponent {
    public Texture texture;

    public View(Texture texture) {
        this.texture = texture;
    }
}

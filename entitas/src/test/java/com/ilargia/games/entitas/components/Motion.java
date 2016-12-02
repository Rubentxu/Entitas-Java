package com.ilargia.games.entitas.components;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"})
public class Motion implements IComponent {
    public Vector2 velocity;

    public Motion(float x, float y) {
        this.velocity = new Vector2(x, y);
    }


}

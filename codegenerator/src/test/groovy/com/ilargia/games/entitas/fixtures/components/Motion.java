package com.ilargia.games.entitas.fixtures.components;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.annotations.Contexts;

public class Motion implements IComponent {
    public Vector2 velocity;

    public Motion(float x, float y) {
        this.velocity = new Vector2(x, y);
    }


}

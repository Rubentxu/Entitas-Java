package com.indignado.games.states.game.component.actuator;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.indignado.games.states.game.data.Bounds;

@Component(pools = {"Actuator"})
public class TextureActuator implements IComponent {
    public String target;
    public Bounds bounds;
    public int opacity = -1;
    public Boolean flipX;
    public Boolean flipY;
    public Color tint;

}

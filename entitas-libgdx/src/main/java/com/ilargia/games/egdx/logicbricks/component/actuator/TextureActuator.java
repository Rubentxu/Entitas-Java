package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.graphics.Color;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.egdx.logicbricks.data.Bounds;

@Component(pools = {"Actuator"})
public class TextureActuator implements IComponent {
    public String target;
    public Bounds bounds;
    public int opacity = -1;
    public Boolean flipX;
    public Boolean flipY;
    public Color tint;

}

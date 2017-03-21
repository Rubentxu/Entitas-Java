package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Actuator"})
public class CameraActuator implements IComponent {
    public short height = 0;
    public float damping = 0.08f;
    public Vector2 minDistance = new Vector2();
    public String followTagEntity;

}

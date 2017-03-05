package com.ilargia.games.logicbrick.component.actuator;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Actuator"})
public class CameraActuator implements IComponent {
    public short height = 0;
    public float damping = 0.08f;
    public String followTagEntity;

}

package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.ilargia.games.egdx.logicbricks.data.StateCharacter;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"}, isSingleEntity = true)
public class DragActuator implements IComponent {
    public int targetEntity;
    public boolean collideConnected ;
    public float maxForce;

}

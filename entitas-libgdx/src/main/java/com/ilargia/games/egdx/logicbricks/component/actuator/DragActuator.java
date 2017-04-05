package com.ilargia.games.egdx.logicbricks.component.actuator;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"}, isSingleEntity = true)
public class DragActuator implements IComponent {
    public int targetEntity;
    public boolean collideConnected ;
    public float maxForce;

    public DragActuator(int targetEntity, boolean collideConnected, float maxForce) {
        this.targetEntity = targetEntity;
        this.collideConnected = collideConnected;
        this.maxForce = maxForce;
    }


}

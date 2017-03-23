package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.ilargia.games.egdx.logicbricks.data.StateCharacter;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class DropActuator implements IComponent {
    public String target;
    public float maxForce;

}

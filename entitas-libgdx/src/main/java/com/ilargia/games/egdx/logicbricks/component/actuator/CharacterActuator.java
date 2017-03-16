package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.egdx.logicbricks.data.StateCharacter;

@Component(pools = {"Actuator"})
public class CharacterActuator implements IComponent {
    public String target;
    public StateCharacter newState;
    public boolean facingLeft;


}

package com.indignado.games.states.game.component.actuator;


import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.indignado.games.states.game.data.StateCharacter;

@Component(pools = {"Actuator"})
public class CharacterActuator implements IComponent {
    public String target;
    public StateCharacter newState;
    public boolean facingLeft;


}

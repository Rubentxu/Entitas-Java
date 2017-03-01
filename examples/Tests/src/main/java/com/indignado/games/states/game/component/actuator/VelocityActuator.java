package com.indignado.games.states.game.component.actuator;


import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class VelocityActuator implements IComponent {
    public String target;
    public Vector2 velocity;
    public float angularVelocity;


}

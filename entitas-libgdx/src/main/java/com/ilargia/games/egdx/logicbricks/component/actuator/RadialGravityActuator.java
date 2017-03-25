package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.egdx.logicbricks.data.interfaces.Actuator;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class RadialGravityActuator implements IComponent {
    public Actuator actuator;
    public float gravity;
    public float radius;
    public float gravityFactor;

    public RadialGravityActuator(float gravity, float radius, float gravityFactor) {
        this.gravity = gravity;
        this.radius = radius;
        this.gravityFactor = gravityFactor;

    }


}

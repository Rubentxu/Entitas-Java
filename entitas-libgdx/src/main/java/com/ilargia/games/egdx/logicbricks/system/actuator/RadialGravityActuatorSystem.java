package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.LogManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.actuator.RadialGravityActuator;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

import java.util.Set;


public class RadialGravityActuatorSystem implements IExecuteSystem {

    private final Group<ActuatorEntity> group;
    private Vector2 debris_position;
    private Vector2 planet_distance;
    private float force;
    private Body body;

    public RadialGravityActuatorSystem(Entitas entitas) {
        this.group = entitas.actuator.getGroup(Matcher.AllOf(ActuatorMatcher.RadialGravityActuator(), ActuatorMatcher.Link()));
        planet_distance = new Vector2();
    }

    @Override
    public void execute(float deltaTime) {
        for (ActuatorEntity e : group.getEntities()) {
            RadialGravityActuator actuator = e.getRadialGravityActuator();
            GameEntity owner = Indexed.getInteractiveEntity(e.getLink().ownerEntity);
            Vector2 planet_position = owner.getRigidBody().body.getWorldCenter();

            SensorEntity gravitySensor = Indexed.getSensorsEntity(e.getLink().ownerEntity, "RadialGravitySensor");

            for (int index : gravitySensor.getNearSensor().distanceContactList) {
                GameEntity gameEntity = Indexed.getInteractiveEntity(index);
                body = gameEntity.getRigidBody().body;
                debris_position = body.getWorldCenter();

                planet_distance.set(0, 0);
                planet_distance.add(debris_position);
                planet_distance.sub(planet_position);
                force = -(float) ((actuator.gravity * body.getMass()) / planet_distance.len());

                if (planet_distance.len() < actuator.radius * actuator.gravityFactor) {
                    planet_distance.scl(force);

                    body.applyForceToCenter(planet_distance, true);

                    float desiredAngle = MathUtils.atan2(-body.getLinearVelocity().x, body.getLinearVelocity().y);
                    while (desiredAngle < -180 * MathUtils.degreesToRadians)
                        desiredAngle += 360 * MathUtils.degreesToRadians;
                    while (desiredAngle > 180 * MathUtils.degreesToRadians)
                        desiredAngle -= 360 * MathUtils.degreesToRadians;

                    body.applyTorque(desiredAngle < 0 ? planet_distance.nor().len() / 2 : -planet_distance.nor().len() / 2, true);
                }

            }

        }
    }

}



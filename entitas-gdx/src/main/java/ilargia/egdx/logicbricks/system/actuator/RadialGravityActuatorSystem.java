package ilargia.egdx.logicbricks.system.actuator;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import ilargia.egdx.logicbricks.component.actuator.RadialGravityActuator;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorEntity;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.system.IExecuteSystem;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;


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



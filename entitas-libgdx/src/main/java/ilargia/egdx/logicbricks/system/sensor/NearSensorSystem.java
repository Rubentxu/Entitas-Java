package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import ilargia.egdx.api.Engine;
import ilargia.egdx.api.managers.listener.Collision;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.component.sensor.NearSensor;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;

public class NearSensorSystem extends SensorSystem implements Collision<Fixture>, IInitializeSystem {

    private final Group<SensorEntity> sensorGroup;
    private final Engine engine;

    public NearSensorSystem(Entitas entitas, Engine engine) {
        this.engine = engine;
        this.sensorGroup = entitas.sensor.getGroup(Matcher.AllOf(SensorMatcher.NearSensor(), SensorMatcher.Link()));

    }

    @Override
    public void initialize() {
        engine.getManager(PhysicsManagerGDX.class).addListener(this);
    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        boolean isActive = false;
        NearSensor sensor = sensorEntity.getNearSensor();
        if (sensor.distanceContactList.size() > 0) {
            isActive = true;
            if (!sensor.initContact) sensor.initContact = true;

        } else if (sensor.initContact && sensor.resetDistanceContactList.size() > 0) {
            isActive = true;

        } else if (sensor.initContact) {
            sensor.initContact = false;

        }
        return isActive;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

    @Override
    public void processCollision(Fixture colliderA, Fixture colliderB, boolean collisionSignal) {
        if (colliderA.isSensor() && !colliderB.isSensor()) {
            Integer indexEntityA = (Integer) colliderA.getBody().getUserData();
            Integer indexEntityB = (Integer) colliderB.getBody().getUserData();
            String tagSensorA = (String) colliderA.getUserData();
            Body bodyB = colliderB.getBody();

            for (Fixture fixture : bodyB.getFixtureList()) {
                if(fixture.isSensor()) return;
            }

            if (indexEntityA != null && indexEntityB != null && tagSensorA != null) {
                GameEntity entityA = Indexed.getInteractiveEntity(indexEntityA);
                GameEntity entityB = Indexed.getInteractiveEntity(indexEntityB);
                if (entityA != null && entityB != null && tagSensorA != null) {
                    for (SensorEntity entity : sensorGroup.getEntities()) {
                        if (entity.getLink().ownerEntity == indexEntityA) {
                            NearSensor sensor = entity.getNearSensor();
                            if (sensor.targetTag != null && entityB.getTags().values.contains(sensor.targetTag)) {
                                if (collisionSignal) {
                                    if (tagSensorA.equals("NearSensor")) {
                                        sensor.distanceContactList.add(indexEntityB);
                                        if (entity.getLink().sensorReference.contains("RadialGravity")) {
                                            bodyB.setGravityScale(0);
                                            bodyB.resetMassData();
                                        }

                                    } else if (tagSensorA.equals("ResetNearSensor")) {
                                        sensor.resetDistanceContactList.add(indexEntityB);
                                    }

                                } else {
                                    if (tagSensorA.equals("NearSensor")) {
                                        sensor.distanceContactList.remove(indexEntityB);
                                        if (entity.getLink().sensorReference.contains("RadialGravity")) {
                                            bodyB.setGravityScale(1);
                                            bodyB.resetMassData();
                                        }
                                    } else if (tagSensorA.equals("ResetNearSensor")) {
                                        sensor.resetDistanceContactList.remove(indexEntityB);
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}


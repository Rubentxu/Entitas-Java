package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.physics.box2d.Fixture;
import ilargia.egdx.api.Engine;
import ilargia.egdx.api.managers.listener.Collision;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.component.sensor.RadarSensor;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorContext;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;

public class RadarSensorSystem extends SensorSystem implements Collision<Fixture>, IInitializeSystem {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final Engine engine;


    public RadarSensorSystem(Entitas entitas, Engine engine) {
        this.sensorContex = entitas.sensor;
        this.engine = engine;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.RadarSensor(), SensorMatcher.Link()));

    }

    @Override
    public void initialize() {
        engine.getManager(PhysicsManagerGDX.class).addListener(this);
    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        return sensorEntity.getRadarSensor().collisionSignal;

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

            if (indexEntityA != null && indexEntityB != null && tagSensorA != null && tagSensorA.equals("RadarSensor")) {
                GameEntity entityB = Indexed.getInteractiveEntity(indexEntityB);
                if (entityB != null) {
                    for (SensorEntity entity : sensorGroup.getEntities()) {
                        RadarSensor radar = entity.getRadarSensor();
                        if (entityB.getTags().values.contains(radar.targetTag)) {
                            if (collisionSignal) {
                                Indexed.addEntityInSensor(entity, entityB);
                            } else {
                                Indexed.removeEntityInSensor(entity, entityB);
                            }
                            radar.collisionSignal = collisionSignal;
                        }

                    }
                }
            }
        }
    }
}


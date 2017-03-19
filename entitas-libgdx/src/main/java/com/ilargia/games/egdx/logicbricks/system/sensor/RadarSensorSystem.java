package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.egdx.logicbricks.component.sensor.RadarSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class RadarSensorSystem extends SensorSystem implements Collision {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final GameContext gameContex;

    public RadarSensorSystem(Entitas entitas) {
        this.sensorContex = entitas.sensor;
        this.gameContex = entitas.game;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.RadarSensor(), SensorMatcher.Link()));

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
    public void processCollision(Integer indexEntityA, Integer indexEntityB, boolean collisionSignal) {

    }

    @Override
    public void processSensorCollision(Integer indexEntityA, Integer indexEntityB, String tagSensorA, boolean collisionSignal) {
        if (indexEntityA != null && indexEntityB != null && tagSensorA.equals("RadarSensor")) {
            GameEntity entityA = Indexed.getInteractiveEntity(indexEntityA);
            GameEntity entityB =  Indexed.getInteractiveEntity(indexEntityB);
            if (entityA != null && entityB != null) {
                for (SensorEntity entity :  Indexed.getSensorsEntities(entityA)) {
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


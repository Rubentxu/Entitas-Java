package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.egdx.logicbricks.component.sensor.RadarSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.GameIndex;
import com.ilargia.games.egdx.logicbricks.index.SensorIndex;
import com.ilargia.games.egdx.logicbricks.index.SimpleGameIndex;

public class RadarSensorSystem extends SensorSystem implements IExecuteSystem, Collision {
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
            GameEntity entityA = SimpleGameIndex.getGameEntity(gameContex, indexEntityA);
            GameEntity entityB = SimpleGameIndex.getGameEntity(gameContex, indexEntityB);
            if (entityA != null && entityB != null) {
                for (SensorEntity entity : SensorIndex.getSensors(sensorContex, entityA)) {
                    RadarSensor radar = entity.getRadarSensor();
                    if (entityB.getIdentity().tags.contains(radar.targetTag)) {
                        if (collisionSignal) {
                            GameIndex.addGameEntity(gameContex, entity.getCreationIndex(), entityB);
                        } else {
                            GameIndex.removeGameEntity(gameContex, entity.getCreationIndex(), entityB);
                        }
                        radar.collisionSignal = collisionSignal;
                    }
                }
            }
        }
    }

}


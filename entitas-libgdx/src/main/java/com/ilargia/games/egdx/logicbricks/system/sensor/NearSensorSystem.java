package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.egdx.logicbricks.component.sensor.NearSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.GameIndex;
import com.ilargia.games.egdx.logicbricks.index.SensorIndex;
import com.ilargia.games.egdx.logicbricks.index.SimpleGameIndex;

public class NearSensorSystem extends SensorSystem implements IExecuteSystem, Collision {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final GameContext gameContex;

    public NearSensorSystem(Entitas entitas) {
        this.sensorContex = entitas.sensor;
        this.gameContex = entitas.game;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.NearSensor(), SensorMatcher.Link()));

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
    public void processSensorCollision(Integer indexEntityA, Integer indexEntityB, String tagSensorA, boolean collisionSignal) {
        if (indexEntityA != null && indexEntityB != null) {
            GameEntity entityA = SimpleGameIndex.getGameEntity(gameContex, indexEntityA);
            GameEntity entityB = SimpleGameIndex.getGameEntity(gameContex, indexEntityB);
            if (entityA != null && entityB != null && tagSensorA != null) {
                for (SensorEntity entity : SensorIndex.getSensors(sensorContex, entityA)) {
                    NearSensor sensor = entity.getNearSensor();
                    if (entityB.getIdentity().tags.contains(sensor.targetTag)) {
                        if (collisionSignal) {
                            if (tagSensorA.equals("NearSensor")) {
                                sensor.distanceContactList.add(indexEntityB);
                            } else if (tagSensorA.equals("ResetNearSensor")) {
                                sensor.resetDistanceContactList.add(indexEntityB);
                            }

                        } else {
                            if (tagSensorA.equals("NearSensor")) {
                                sensor.distanceContactList.remove(indexEntityB);
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


package com.ilargia.games.logicbrick.system.sensor;


import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.logicbrick.component.sensor.CollisionSensor;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;
import com.ilargia.games.logicbrick.index.GameIndex;
import com.ilargia.games.logicbrick.index.SensorIndex;

public class CollisionSensorSystem extends SensorSystem implements IExecuteSystem, Collision<GameEntity> {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final GameContext gameContex;

    public CollisionSensorSystem(Entitas entitas) {
        this.sensorContex = entitas.sensor;
        this.gameContex = entitas.game;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.CollisionSensor(), SensorMatcher.Link()));

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        return sensorEntity.getCollisionSensor().collisionSignal;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

    @Override
    public void processCollision(GameEntity entityA, GameEntity entityB, boolean collisionSignal) {
        if(entityA != null && entityB !=null) {
            for (SensorEntity entity : SensorIndex.getSensors(sensorContex, entityA)) {
                CollisionSensor collision = entity.getCollisionSensor();
                if(entityB.getIdentity().tags.contains(collision.targetTag)) {
                    if(collisionSignal) {
                        GameIndex.addGameEntity(gameContex, entity.getCreationIndex(), entityB);
                    } else {
                        GameIndex.removeGameEntity(gameContex, entity.getCreationIndex(), entityB);
                    }
                    collision.collisionSignal = collisionSignal;
                }
            }
        }
    }

}


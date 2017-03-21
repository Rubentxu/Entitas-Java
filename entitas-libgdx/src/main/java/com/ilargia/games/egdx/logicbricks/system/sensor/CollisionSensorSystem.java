package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.LogManagerGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.sensor.CollisionSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class CollisionSensorSystem extends SensorSystem implements IInitializeSystem, Collision {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final EngineGDX engine;

    public CollisionSensorSystem(Entitas entitas, EngineGDX engine) {
        this.sensorContex = entitas.sensor;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.CollisionSensor(), SensorMatcher.Link()));
        this.engine = engine;
    }

    @Override
    public void initialize() {
        engine.getManager(PhysicsManagerGDX.class).addListener(this);
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
    public void processCollision(Integer indexEntityA, Integer indexEntityB, boolean collisionSignal) {
        if (indexEntityA != null && indexEntityB != null) {
            GameEntity entityA =  Indexed.getInteractiveEntity(indexEntityA);
            GameEntity entityB =  Indexed.getInteractiveEntity(indexEntityB);
            if (entityA != null && entityB != null) {
                for (SensorEntity entity : Indexed.getSensorsEntities(entityA)) {
                    CollisionSensor collision = entity.getCollisionSensor();
                    if (entityB.getTags().values.contains(collision.targetTag)) {
                        if (collisionSignal) {
                            Indexed.addEntityInSensor(entity, entityB);
                        } else {
                            Indexed.removeEntityInSensor(entity, entityB);
                        }
                        collision.collisionSignal = collisionSignal;
                    }
                }
            }
        }
    }

    @Override
    public void processSensorCollision(Integer entityA, Integer entityB, String tagSensorA, boolean collisionSignal) {

    }


}


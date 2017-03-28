package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ilargia.games.egdx.api.managers.listener.Collision;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.sensor.CollisionSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class CollisionSensorSystem extends SensorSystem implements IInitializeSystem, Collision<Fixture> {
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
    public void processCollision(Fixture colliderA, Fixture colliderB, boolean collisionSignal) {
        Integer indexEntityA = (Integer) colliderA.getBody().getUserData();
        Integer indexEntityB = (Integer) colliderB.getBody().getUserData();
        if (indexEntityA != null && indexEntityB != null) {
            GameEntity entityA =  Indexed.getInteractiveEntity(indexEntityA);
            GameEntity entityB =  Indexed.getInteractiveEntity(indexEntityB);
            if (entityA != null && entityB != null) {
                for (SensorEntity entity : sensorGroup.getEntities()) {
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
}


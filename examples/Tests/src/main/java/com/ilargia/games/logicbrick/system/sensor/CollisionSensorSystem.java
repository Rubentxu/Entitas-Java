package com.ilargia.games.logicbrick.system.sensor;


import com.badlogic.gdx.physics.box2d.*;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.logicbrick.component.sensor.CollisionSensor;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;
import com.ilargia.games.logicbrick.index.SensorIndex;

public class CollisionSensorSystem extends SensorSystem implements IExecuteSystem, ContactListener {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;

    public CollisionSensorSystem(Entitas entitas) {
        this.sensorContex = entitas.sensor;
        sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.DelaySensor(), SensorMatcher.Signal()));

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
    private void processCollision(Object dataA, Object dataB, boolean signal) {
        if(dataA != null && dataB !=null) {
            GameEntity entityA = (GameEntity) dataA;
            GameEntity entityB = (GameEntity) dataB;
            for (SensorEntity entity : SensorIndex.getEntitiesSensor(sensorContex, entityA)) {
                CollisionSensor collision = entity.getCollisionSensor();
                if(entityB.getIdentity().tags.contains(collision.targetTag))
                    collision.collisionSignal = signal;

            }
        }
    }

    @Override
    public void beginContact(Contact contact) {
        Object dataA = contact.getFixtureA().getBody().getUserData();
        Object dataB = contact.getFixtureB().getBody().getUserData();

        processCollision(dataA, dataB, true);
        processCollision(dataB, dataA, true);

    }

    @Override
    public void endContact(Contact contact) {
        Object dataA = contact.getFixtureA().getBody().getUserData();
        Object dataB = contact.getFixtureB().getBody().getUserData();

        processCollision(dataA, dataB, false);
        processCollision(dataB, dataA, false);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}


package com.ilargia.games.logicbrick.system.sensor;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.base.managers.BasePhysicsManager;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.logicbrick.component.sensor.Link;
import com.ilargia.games.logicbrick.component.sensor.RaySensor;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;
import com.ilargia.games.logicbrick.index.GameIndex;
import com.ilargia.games.logicbrick.index.SimpleGameIndex;

public class RaySensorSystem extends SensorSystem implements IExecuteSystem {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final GameContext gameContex;
    private final BasePhysicsManager physics;

    public RaySensorSystem(Entitas entitas, Engine engine) {
        this.sensorContex = entitas.sensor;
        this.gameContex = entitas.game;
        this.physics = engine.getManager(BasePhysicsManager.class);
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.RaySensor(), SensorMatcher.Link()));

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        RaySensor sensor = sensorEntity.getRaySensor();
        sensor.collisionSignal = true;
        Link link = sensorEntity.getLink();

        float angle = sensor.axis2D.ordinal() * 90.0f;
        GameEntity originEntity = SimpleGameIndex.getGameEntity(gameContex, link.targetEntity);


        Vector2 point1 = originEntity.getRigidBody().body.getPosition();
        Vector2 point2 = point1.cpy().add(new Vector2((float) MathUtils.cosDeg(angle), MathUtils.sinDeg(angle)).scl(sensor.range));

        physics.getPhysics().rayCast((Fixture fixture, Vector2 point, Vector2 normal, float fraction)-> {
            Integer indexEntity = (Integer) fixture.getBody().getUserData();
            GameEntity entity = SimpleGameIndex.getGameEntity(gameContex, indexEntity);

            if (sensor.targetTag != null && entity.getIdentity().tags.contains(sensor.targetTag)) {
                GameIndex.addGameEntity(gameContex, sensorEntity.getCreationIndex(), entity);
                sensor.collisionSignal = true;
            } else if (sensor.targetTag == null ) {
                GameIndex.addGameEntity(gameContex, sensorEntity.getCreationIndex(), entity);
                sensor.collisionSignal = true;
            }

            if (sensor.xRayMode) return 1;
            else return 0;

        }, point1, point2);

        return sensor.collisionSignal;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

}


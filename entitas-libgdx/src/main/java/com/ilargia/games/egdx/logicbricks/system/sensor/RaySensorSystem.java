package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.egdx.logicbricks.component.sensor.Link;
import com.ilargia.games.egdx.logicbricks.component.sensor.RaySensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.SimpleGameIndex;

public class RaySensorSystem extends SensorSystem implements IExecuteSystem {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;
    private final GameContext gameContex;
    private final World physics;

    public RaySensorSystem(Entitas entitas, World physisc) {
        this.sensorContex = entitas.sensor;
        this.gameContex = entitas.game;
        this.physics = physisc;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.RaySensor(), SensorMatcher.Link()));

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        RaySensor sensor = sensorEntity.getRaySensor();
        sensor.rayContactList.clear();
        sensor.collisionSignal = false;
        Link link = sensorEntity.getLink();

        float angle = sensor.axis2D.ordinal() * 90.0f;
        GameEntity originEntity = SimpleGameIndex.getGameEntity(gameContex, link.targetEntity);

        Vector2 point1 = originEntity.getRigidBody().body.getPosition();
        Vector2 point2 = point1.cpy().add(new Vector2((float) MathUtils.cosDeg(angle), MathUtils.sinDeg(angle)).scl(sensor.range));

        physics.rayCast((fixture, point, normal, fraction) -> reportRayFixture(sensor, fixture), point1, point2);
        return sensor.collisionSignal;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

    float reportRayFixture (RaySensor sensor, Fixture fixture){
        Integer indexEntity = (Integer) fixture.getBody().getUserData();
        sensor.collisionSignal = false;
        GameEntity entity = SimpleGameIndex.getGameEntity(gameContex, indexEntity);

        if (sensor.targetTag != null && entity.getIdentity().tags.contains(sensor.targetTag)) {
            sensor.rayContactList.add(indexEntity);
            sensor.collisionSignal = true;
        } else if (sensor.targetTag == null ) {
            sensor.rayContactList.add(indexEntity);
            sensor.collisionSignal = true;
        }

        if (sensor.xRayMode) return 1;
        else return 0;
    }

}


package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.logicbricks.data.RigidBody;
import com.ilargia.games.egdx.logicbricks.component.sensor.NearSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class CreateNearSensorSystem extends ReactiveSystem<SensorEntity> {
    private final GameContext gameContext;
    private final BodyBuilder bodyBuilder;

    public CreateNearSensorSystem(Entitas entitas, Engine engine) {
        super(entitas.sensor);
        this.gameContext = entitas.game;
        this.bodyBuilder = engine.getManager(PhysicsManagerGDX.class).getBodyBuilder();
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(SensorMatcher.NearSensor());
    }

    @Override
    protected boolean filter(SensorEntity entity) {
        return entity.hasNearSensor();
    }

    @Override
    protected void execute(List<SensorEntity> entities) {
        for (SensorEntity e : entities) {
            GameEntity gameEntity =  Indexed.getInteractiveEntity(e.getLink().ownerEntity);
            RigidBody rigidBody = gameEntity.getRigidBody();
            NearSensor nearSensor = e.getNearSensor();

            if (nearSensor.distance > 0) {
                FixtureDef nearFixture = bodyBuilder.fixtureDefBuilder()
                        .circleShape(nearSensor.distance)
                        .sensor()
                        .build();
                rigidBody.body.createFixture(nearFixture).setUserData("NearSensor");

                if (nearSensor.resetDistance > nearSensor.distance) {
                    FixtureDef nearResetFixture = bodyBuilder.fixtureDefBuilder()
                            .circleShape(nearSensor.resetDistance)
                            .sensor()
                            .build();
                    rigidBody.body.createFixture(nearResetFixture).setUserData("ResetNearSensor");
                }
            }
        }

    }


}

package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.physics.box2d.FixtureDef;
import ilargia.egdx.api.Engine;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.component.game.RigidBody;
import ilargia.egdx.logicbricks.component.sensor.NearSensor;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameContext;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.egdx.util.BodyBuilder;
import ilargia.entitas.api.IContext;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.systems.ReactiveSystem;

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

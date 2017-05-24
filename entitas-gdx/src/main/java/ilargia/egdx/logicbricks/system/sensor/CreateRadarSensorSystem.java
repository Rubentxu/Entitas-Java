package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import ilargia.egdx.api.Engine;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.logicbricks.component.game.RigidBody;
import ilargia.egdx.logicbricks.component.sensor.RadarSensor;
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

public class CreateRadarSensorSystem extends ReactiveSystem<SensorEntity> {
    private final GameContext gameContext;
    private final BodyBuilder bodyBuilder;

    public CreateRadarSensorSystem(Entitas entitas, Engine engine) {
        super(entitas.sensor);
        this.gameContext = entitas.game;
        this.bodyBuilder = engine.getManager(PhysicsManagerGDX.class).getBodyBuilder();
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(SensorMatcher.RadarSensor());
    }

    @Override
    protected boolean filter(SensorEntity entity) {
        return entity.hasRadarSensor();
    }

    @Override
    protected void execute(List<SensorEntity> entities) {
        Vector2[] vertices = new Vector2[8];
        vertices[0] = new Vector2();

        for (SensorEntity e : entities) {
            GameEntity gameEntity =  Indexed.getInteractiveEntity(e.getLink().ownerEntity);
            RigidBody rigidBody = gameEntity.getRigidBody();
            RadarSensor radarSensor = e.getRadarSensor();

            if (radarSensor.angle < 180) {
                for (int i = 0; i < 7; i++) {
                    float angle = (float) (i / 6.0 * radarSensor.angle) - (radarSensor.angle / 2) + (radarSensor.axis2D.ordinal() * 90);

                    vertices[i + 1] = new Vector2(radarSensor.distance * MathUtils.cosDeg(angle), radarSensor.distance * MathUtils.sinDeg(angle));
                }
                FixtureDef radarFixture = bodyBuilder.fixtureDefBuilder()
                        .polygonShape(vertices)
                        .sensor()
                        .build();
                rigidBody.body.createFixture(radarFixture).setUserData("RadarSensor");

            }
        }

    }

}

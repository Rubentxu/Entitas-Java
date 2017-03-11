package com.ilargia.games.logicbrick.system.sensor;


import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.base.managers.BasePhysicsManager;
import com.ilargia.games.egdx.base.util.BodyBuilder;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.logicbrick.component.sensor.NearSensor;
import com.ilargia.games.logicbrick.gen.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;
import com.ilargia.games.logicbrick.index.GameIndex;
import com.ilargia.games.logicbrick.index.SensorIndex;
import com.ilargia.games.logicbrick.index.SimpleGameIndex;

import java.util.List;
import java.util.Set;

public class CreateNearSensorSystem extends ReactiveSystem<SensorEntity> {
    private final GameContext gameContext;
    private final Engine engine;
    private final BodyBuilder bodyBuilder;

    public CreateNearSensorSystem(Entitas entitas, Engine engine) {
        super(entitas.sensor);
        this.gameContext = entitas.game;
        this.engine = engine;
        this.bodyBuilder = engine.getManager(BasePhysicsManager.class).getBodyBuilder();
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
            NearSensor nearSensor = e.getNearSensor();
            Set<GameEntity> gameEntity = SimpleGameIndex.getGameEntity(gameContext, e);

            FixtureDef nearFixture = bodyBuilder.fixtureDefBuilder()
                    .circleShape(nearSensor.distance)
                    .sensor()
                    .build();

            FixtureDef nearResetFixture = bodyBuilder.fixtureDefBuilder()
                    .circleShape(nearSensor.resetDistance)
                    .sensor()
                    .build();
            

        }

    }


}

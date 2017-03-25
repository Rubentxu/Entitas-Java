package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.component.actuator.RadialGravityActuator;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class CreateRadialGravityActuatorSystem extends ReactiveSystem<ActuatorEntity> {
    private final EngineGDX engine;
    private final Entitas entitas;

    public CreateRadialGravityActuatorSystem(Entitas entitas, EngineGDX engine) {
        super(entitas.actuator);
        this.entitas = entitas;
        this.engine = engine;

    }

    @Override
    protected Collector<ActuatorEntity> getTrigger(IContext<ActuatorEntity> context) {
        return context.createCollector(ActuatorMatcher.RadialGravityActuator());
    }

    @Override
    protected boolean filter(ActuatorEntity entity) {
        return entity.hasRadialGravityActuator();
    }

    @Override
    protected void execute(List<ActuatorEntity> actuatorEntities) {
        for (ActuatorEntity e : actuatorEntities) {
            RadialGravityActuator actuator = e.getRadialGravityActuator();
            GameEntity owner = Indexed.getInteractiveEntity(e.getLink().ownerEntity);
            entitas.sensor.createEntity()
                    .addNearSensor("",actuator.radius, 0)
                    .addLink(owner.getCreationIndex(), "RadialGravitySensor");


        }
    }

}



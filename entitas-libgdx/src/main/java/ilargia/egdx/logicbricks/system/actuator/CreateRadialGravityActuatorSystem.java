package ilargia.egdx.logicbricks.system.actuator;


import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.logicbricks.component.actuator.RadialGravityActuator;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorEntity;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.IContext;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.systems.ReactiveSystem;

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
                    .addLink("RadialGravitySensor", owner.getCreationIndex());


        }
    }

}



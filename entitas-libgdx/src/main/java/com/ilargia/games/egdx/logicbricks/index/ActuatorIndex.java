package com.ilargia.games.egdx.logicbricks.index;

import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorContext;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;

import java.util.Set;

public class ActuatorIndex {
    public static final String ActuatorKey = "Actuators";

    public static void createSensorEntityIndices(ActuatorContext context) {
        EntityIndex<ActuatorEntity, Integer> eIndex = new EntityIndex<ActuatorEntity, Integer>(((e, c) -> e.getCreationIndex()),
                context.getGroup(ActuatorMatcher.Link()));
        context.addEntityIndex(ActuatorKey, eIndex);
    }

    public static void addActuatorEntity(ActuatorContext context, Integer index, ActuatorEntity entity) {
        EntityIndex<ActuatorEntity, Integer> eIndex = (EntityIndex<ActuatorEntity, Integer>) context.getEntityIndex(ActuatorKey);
        eIndex.addEntity(index, entity);
    }

    public static Set<ActuatorEntity> getEntitiesActuator(ActuatorContext context, GameEntity entity) {
        EntityIndex<ActuatorEntity, Integer> eIndex = (EntityIndex<ActuatorEntity, Integer>) context.getEntityIndex(ActuatorKey);
        return eIndex.getEntities(entity.getCreationIndex());
    }

}

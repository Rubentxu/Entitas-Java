package com.ilargia.games.logicbrick.index;

import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;

import java.util.Set;

public class SensorIndex {
    public static final String SensorKey = "Sensors";

    public static void createSensorEntityIndices(SensorContext context) {
        EntityIndex<SensorEntity, Integer> eIndex = new EntityIndex<SensorEntity, Integer>(((e, c) -> e.getCreationIndex()),
                context.getGroup(SensorMatcher.Link()));
        context.addEntityIndex(SensorKey, eIndex);
    }

    public static void addSensorEntity(SensorContext context, Integer index, SensorEntity entity) {
        EntityIndex<SensorEntity, Integer> eIndex = (EntityIndex<SensorEntity, Integer>) context.getEntityIndex(SensorKey);
        eIndex.addEntity(index, entity);
    }

    public static Set<SensorEntity> getEntitiesSensor(SensorContext context, GameEntity entity) {
        EntityIndex<SensorEntity, Integer> eIndex = (EntityIndex<SensorEntity, Integer>) context.getEntityIndex(SensorKey);
        return eIndex.getEntities(entity.getCreationIndex());
    }

}

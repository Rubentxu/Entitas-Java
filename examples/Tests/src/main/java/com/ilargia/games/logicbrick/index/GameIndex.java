package com.ilargia.games.logicbrick.index;

import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;

import java.util.Set;

public class GameIndex {
    public static final String GameKey = "GameEntities";

    public static void createSensorEntityIndices(GameContext context) {
        EntityIndex<GameEntity, Integer> eIndex = new EntityIndex<GameEntity, Integer>(((e, c) -> e.getCreationIndex()),
                context.getGroup(GameMatcher.Identity()));
        context.addEntityIndex(GameKey, eIndex);
    }

    public static void addGameEntity(GameContext context, Integer index, GameEntity entity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        eIndex.addEntity(index, entity);
    }

    public static Set<GameEntity> getEntitiesGame(GameContext context, SensorEntity entity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        return eIndex.getEntities(entity.getCreationIndex());
    }

}

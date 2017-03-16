package com.ilargia.games.egdx.logicbricks.index;

import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;

import java.util.Set;

public class GameIndex {
    public static final String GameKey = "GameEntities";

    public static void createGameEntitiesIndices(GameContext context) {
        EntityIndex<GameEntity, Integer> eIndex = new EntityIndex<GameEntity, Integer>(((e, c) -> e.getCreationIndex()),
                context.getGroup(GameMatcher.Identity()));
        context.addEntityIndex(GameKey, eIndex);
    }

    public static void addGameEntity(GameContext context, Integer index, GameEntity entity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        eIndex.addEntity(index, entity);
    }

    public static void removeGameEntity(GameContext context, Integer index, GameEntity entity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        eIndex.removeEntity(index, entity);
    }

    public static Set<GameEntity> getGameEntities(GameContext context, SensorEntity entity) {
        EntityIndex<GameEntity, Integer> eIndex = (EntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        return eIndex.getEntities(entity.getCreationIndex());
    }


}

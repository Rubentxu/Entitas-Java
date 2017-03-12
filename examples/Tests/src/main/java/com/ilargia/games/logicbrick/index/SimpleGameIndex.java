package com.ilargia.games.logicbrick.index;

import com.ilargia.games.entitas.index.EntityIndex;
import com.ilargia.games.entitas.index.PrimaryEntityIndex;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;

import java.util.Set;

public class SimpleGameIndex {
    public static final String GameKey = "GameEntity";

    public static void createGameEntityIndices(GameContext context) {
        PrimaryEntityIndex<GameEntity, Integer> eIndex = new PrimaryEntityIndex<GameEntity, Integer>(((e, c) -> e.getCreationIndex()),
                context.getGroup(GameMatcher.Identity()));
        context.addEntityIndex(GameKey, eIndex);
    }

    public static void addGameEntity(GameContext context, GameEntity entity) {
        PrimaryEntityIndex<GameEntity, Integer> eIndex = (PrimaryEntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        eIndex.addEntity(entity.getCreationIndex(), entity);
    }

    public static void removeGameEntity(GameContext context, Integer index, GameEntity entity) {
        PrimaryEntityIndex<GameEntity, Integer> eIndex = (PrimaryEntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        eIndex.removeEntity(index, entity);
    }

    public static GameEntity getGameEntity(GameContext context, Integer indexEntity) {
        PrimaryEntityIndex<GameEntity, Integer> eIndex = (PrimaryEntityIndex<GameEntity, Integer>) context.getEntityIndex(GameKey);
        return eIndex.getEntity(indexEntity);
    }


}

package com.ilargia.games;

import com.ilargia.games.components.Position;
import com.ilargia.games.core.Entitas;
import com.ilargia.games.core.GameContext;
import com.ilargia.games.core.GameEntity;
import com.ilargia.games.core.GameMatcher;
import com.ilargia.games.entitas.index.EntityIndex;

import java.util.Set;


public class EntityIndexExtension {

    public static final String PositionKey = "Position";
    final int shiftX = 8;

    public static void addEntityIndices(Entitas contexts) {
        EntityIndex<GameEntity, Integer> positionIndex = new EntityIndex(
                contexts.game.getGroup(GameMatcher.Position()),
                (e, c) -> {
                    Position positionComponent = (Position) c;
                    return positionComponent != null
                            ? positionComponent.x + "," + positionComponent.y
                            : ((GameEntity) e).getPosition().x + "," + ((GameEntity) e).getPosition().y;
                }
        );
        contexts.game.addEntityIndex(PositionKey, positionIndex);
    }

    public static Set<GameEntity> getEntitiesWithPosition(GameContext context, float x, float y) {
        EntityIndex<GameEntity, String> index = (EntityIndex<GameEntity, String>) context.getEntityIndex(PositionKey);
        return index.getEntities(x + "," + y);
    }
}

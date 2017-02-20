package com.ilargia.games;

import com.ilargia.games.components.game.Position;
import com.ilargia.games.core.Entitas;
import com.ilargia.games.core.game.GameContext;
import com.ilargia.games.core.game.GameEntity;
import com.ilargia.games.core.game.GameMatcher;
import com.ilargia.games.entitas.index.EntityIndex;

import java.util.Set;


public class EntityIndexExtension {

    public static final String PositionKey = "Position";
    static final int shiftX = 8;

    public static void addEntityIndices(Entitas contexts) {
        EntityIndex<GameEntity, Integer> positionIndex = new EntityIndex(
                contexts.game.getGroup(GameMatcher.Position()),
                (e, c) -> {
                    Position positionComponent = (Position) c;
                    return positionComponent != null
                            ? (positionComponent.x << shiftX) + positionComponent.y
                            : (((GameEntity) e).getPosition().x << shiftX) + ((GameEntity) e).getPosition().y;
                }
        );
        contexts.game.addEntityIndex(PositionKey, positionIndex);
    }

    public static Set<GameEntity> getEntitiesWithPosition(GameContext context, int x, int y) {
        EntityIndex<GameEntity, Integer> index = (EntityIndex<GameEntity, Integer>) context.getEntityIndex(PositionKey);
        return index.getEntities((x << shiftX) + y - 1);
    }
}

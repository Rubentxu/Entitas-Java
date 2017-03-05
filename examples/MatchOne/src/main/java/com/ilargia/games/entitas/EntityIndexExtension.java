package com.ilargia.games.entitas;

import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.component.game.Position;
import com.ilargia.games.entitas.core.Entitas;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
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

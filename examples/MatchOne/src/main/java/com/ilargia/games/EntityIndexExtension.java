package com.ilargia.games;

import com.ilargia.games.components.Position;
import com.ilargia.games.entitas.index.EntityIndex;


public class EntityIndexExtension {

    public static final String PositionKey = "Position";

    public static void addEntityIndices(Entitas contexts) {
        com.ilargia.games.entitas.EntityIndex<String,Entity> positionIndex = new EntityIndex(
                contexts.core.getGroup(CoreMatcher.Position()),
                (e, c) -> {
                    Position positionComponent = (Position) c;
                    return positionComponent != null
                            ? positionComponent.x + "," + positionComponent.y
                            : ((Entity)e).getPosition().x + "," + ((Entity)e).getPosition().y;
                }
        );

        contexts.core.addEntityIndex(PositionKey, positionIndex);
    }

    public static ObjectSet<Entity> getEntitiesWithPosition(Context context, int x, int y) {
        EntityIndex<String,Entity> index = (EntityIndex<String, Entity>) context.getEntityIndex(PositionKey);
        return index.getEntities(x + "," + y);
    }
}

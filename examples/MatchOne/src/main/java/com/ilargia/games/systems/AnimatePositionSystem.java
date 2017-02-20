package com.ilargia.games.systems;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.components.game.Position;
import com.ilargia.games.core.game.GameContext;
import com.ilargia.games.core.game.GameEntity;
import com.ilargia.games.core.game.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class AnimatePositionSystem extends ReactiveSystem<GameEntity> {

    GameContext context;

    public AnimatePositionSystem(GameContext context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.Position());
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasTextureView() && entity.hasPosition();
    }

    @Override
    public void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            Position pos = e.getPosition();
            //e.getTextureView().body.setTransform(new Vector2(0, -12), new Vector2(pos.x, pos.y - 1), true);

            moveFocused(new Vector2(pos.x, pos.y - 1), e.getTextureView().body);
        }

    }

    public void moveFocused(Vector2 center, Body body) {
        MoveData moveData = new MoveData();
        body.setLinearVelocity(new Vector2());
        moveData.start = body.getWorldCenter().cpy();
        moveData.direction = new Vector2(center.x - moveData.start.x, center.y - moveData.start.y);
        moveData.current = 1;
        moveData.total = 10;

        if (moveData.current == moveData.total + 1) {
            body.setLinearVelocity(new Vector2());
            return;
        }

        int t = easeInOut(moveData.current, 0, 1, moveData.total);

        Vector2 sEnd = moveData.direction.cpy();
        sEnd.scl(t);
        sEnd.add(moveData.start.cpy());

        Vector2 sStart = body.getWorldCenter();

        sEnd.sub(sStart);
        sEnd.scl(60);
        moveData.current++;
        body.setLinearVelocity(sEnd);
    }

    private int easeInOut(int t, int b, int c, int d) {
        if ((t /= d / 2) < 1) {
            return c / 2 * t * t * t * t + b;
        }
        return -c / 2 * ((t -= 2) * t * t * t - 2) + b;
    }

    private class MoveData {
        public Vector2 start;
        public Vector2 direction;
        public int current;
        public int total;

    }

}

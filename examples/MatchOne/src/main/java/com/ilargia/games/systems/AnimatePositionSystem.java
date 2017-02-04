package com.ilargia.games.systems;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.ContextExtensions;
import com.ilargia.games.GameBoardLogic;
import com.ilargia.games.components.GameBoard;
import com.ilargia.games.components.Position;
import com.ilargia.games.core.GameContext;
import com.ilargia.games.core.GameEntity;
import com.ilargia.games.core.GameMatcher;
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
        for(GameEntity e : entities) {
            Position pos = e.getPosition();
            e.getTextureView().body.applyForce(new Vector2(0, -2), new Vector2(pos.x, pos.y), true);
        }

    }


}

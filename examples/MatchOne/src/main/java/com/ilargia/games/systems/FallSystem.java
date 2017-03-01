package com.ilargia.games.systems;

import com.ilargia.games.EntityIndexExtension;
import com.ilargia.games.GameBoardLogic;
import com.ilargia.games.core.component.game.GameBoard;
import com.ilargia.games.core.gen.game.GameContext;
import com.ilargia.games.core.gen.game.GameEntity;
import com.ilargia.games.core.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.group.GroupEvent;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class FallSystem extends ReactiveSystem<GameEntity> {
    private GameContext context;

    public FallSystem(GameContext context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return context.createCollector(GameMatcher.GameBoardElement(), GroupEvent.Removed);
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return true;
    }

    @Override
    protected void execute(List<GameEntity> gameEntities) {
        GameBoard gameBoard = context.getGameBoard();

        for (int column = 0; column < gameBoard.columns; column++) {
            for (int row = 1; row < gameBoard.rows; row++) {
                Set<GameEntity> movables = EntityIndexExtension.getEntitiesWithPosition(context, column, row).stream()
                        .filter(e -> e.isMovable())
                        .collect(Collectors.toSet());
                for (GameEntity e : movables) {
                    moveDown(e, column, row);
                }
            }
        }

    }

    void moveDown(GameEntity e, int column, int row) {
        int nextRowPos = GameBoardLogic.getNextEmptyRow(context, column, row);
        if (nextRowPos != row) {
            e.replacePosition(column, nextRowPos);
        }
    }


}

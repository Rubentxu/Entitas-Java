package com.ilargia.games.systems;

import com.ilargia.games.ContextExtensions;
import com.ilargia.games.GameBoardLogic;
import com.ilargia.games.components.game.GameBoard;
import com.ilargia.games.core.GameContext;
import com.ilargia.games.core.GameMatcher;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.group.GroupEvent;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class FillSystem extends ReactiveSystem {

    GameContext context;

    public FillSystem(GameContext context) {
        super(context);
        this.context = context;
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.GameBoardElement(), GroupEvent.Removed);
    }

    @Override
    protected boolean filter(Entity entity) {
        return true;
    }

    @Override
    public void execute(List entities) {
        GameBoard gameBoard = context.getGameBoard();
        for (int column = 0; column < gameBoard.columns; column++) {
            int nextRowPos = GameBoardLogic.getNextEmptyRow(context, column, gameBoard.rows);
            while (nextRowPos != gameBoard.rows) {
                ContextExtensions.createRandomPiece(context, column, nextRowPos);
                nextRowPos = GameBoardLogic.getNextEmptyRow(context, column, gameBoard.rows);
            }
        }

    }


}

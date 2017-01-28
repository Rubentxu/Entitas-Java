package com.ilargia.games.systems;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.GameBoardLogic;
import com.ilargia.games.PoolExtensions;
import com.ilargia.games.components.GameBoard;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class FillSystem implements ISetPool<Pool>, IReactiveSystem {
    private Pool _pool;

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.GameBoardElement().OnEntityRemoved();
    }


    @Override
    public void setPool(Pool pool) {
        _pool = pool;
    }

    @Override
    public void execute(Array entities) {
        GameBoard gameBoard = _pool.getGameBoard();
        for (int column = 0; column < gameBoard.columns; column++) {
            int nextRowPos = GameBoardLogic.getNextEmptyRow(_pool, column, gameBoard.rows);
            while(nextRowPos != gameBoard.rows) {
                PoolExtensions.createRandomPiece(_pool, column, nextRowPos);
                nextRowPos = GameBoardLogic.getNextEmptyRow(_pool, column, gameBoard.rows);
            }
        }

    }

}

package com.ilargia.games.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.PoolExtensions;
import com.ilargia.games.components.GameBoard;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.IInitializeSystem;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class GameBoardSystem implements ISetPool<Pool>,IInitializeSystem, IReactiveSystem<Entity> {
    private Pool _pool;
    Group<Entity> _gameBoardElements;

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.GameBoard().OnEntityAdded();
    }


    @Override
    public void setPool(Pool pool) {
        _pool = pool;
        _gameBoardElements = _pool.getGroup(Matcher.AllOf(CoreMatcher.GameBoardElement(), CoreMatcher.Position()));
    }

    @Override
    public void initialize() {
        GameBoard gameBoard = _pool.setGameBoard(8, 9).getGameBoard();
        for(int row = 0; row < gameBoard.rows; row++) {
            for(int column = 0; column < gameBoard.columns; column++) {
                if(MathUtils.random(0,1) > 0.91f) {
                    PoolExtensions.createBlocker(_pool, column, row);
                } else {
                    PoolExtensions.createRandomPiece(_pool, column, row);
                }
            }
        }
    }

    @Override
    public void execute(Array<Entity> entities) {
        GameBoard gameBoard = _pool.getGameBoard();
        for( Entity e : _gameBoardElements.getEntities()) {
            if(e.getPosition().x >= gameBoard.columns || e.getPosition().y >= gameBoard.rows) {
                e.setDestroy(true);
            }
        }

    }

}

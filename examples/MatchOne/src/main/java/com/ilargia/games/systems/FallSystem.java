package com.ilargia.games.systems;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.EntityIndexExtension;
import com.ilargia.games.GameBoardLogic;
import com.ilargia.games.components.GameBoard;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class FallSystem implements ISetPool<Pool>, IReactiveSystem<Entity> {
    private Pool _pool;

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.GameBoardElement().OnEntityAdded();
    }


    @Override
    public void setPool(Pool pool) {
        _pool = pool;
    }

    @Override
    public void execute(Array entities) {
        GameBoard gameBoard = _pool.getGameBoard();

        for (int column = 0; column < gameBoard.columns; column++) {
            for (int row = 1; row < gameBoard.rows; row++) {
                ObjectSet.ObjectSetIterator<Entity> it = EntityIndexExtension.getEntitiesWithPosition(_pool, column, row).iterator();
                Entity e;
                Array<Entity> movables = new Array<>();
                while (it.hasNext()) {
                    e = it.next();
                    if (e.isMovable())
                        movables.add(e);
                }

                for (Entity mov : movables) {
                    moveDown(mov, column, row);
                }
            }
        }

    }

    void moveDown(Entity e, int column, int row) {
        int nextRowPos = GameBoardLogic.getNextEmptyRow(_pool, column, row);
        if (nextRowPos != row) {
            e.replacePosition(column, nextRowPos);
        }
    }

}

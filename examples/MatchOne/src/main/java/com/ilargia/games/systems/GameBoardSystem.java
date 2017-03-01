package com.ilargia.games.systems;

import com.badlogic.gdx.math.MathUtils;
import com.ilargia.games.ContextExtensions;
import com.ilargia.games.core.component.game.GameBoard;
import com.ilargia.games.core.gen.game.GameContext;
import com.ilargia.games.core.gen.game.GameEntity;
import com.ilargia.games.core.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;


public class GameBoardSystem extends ReactiveSystem<GameEntity> implements IInitializeSystem {

    private final GameContext context;
    private final Group<GameEntity> gameBoardElements;


    public GameBoardSystem(GameContext context) {
        super(context);
        this.context = context;
        this.gameBoardElements = context.getGroup(Matcher.AllOf(GameMatcher.GameBoardElement(), GameMatcher.Position()));
    }

    @Override
    protected Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return context.createCollector(GameMatcher.GameBoard());
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasGameBoard();
    }

    @Override
    public void initialize() {
        GameBoard gameBoard = context.setGameBoard(8, 9).getGameBoard();
        for (int row = 0; row < gameBoard.rows; row++) {
            for (int column = 0; column < gameBoard.columns; column++) {
                if (MathUtils.random(0, 1) > 0.91f) {
                    ContextExtensions.createBlocker(context, column, row);
                } else {
                    ContextExtensions.createRandomPiece(context, column, row);
                }
            }
        }
    }

    @Override
    public void execute(List<GameEntity> entities) {
        GameBoard gameBoard = context.getGameBoard();
        for (GameEntity e : gameBoardElements.getEntities()) {
            if (e.getPosition().x >= gameBoard.columns || e.getPosition().y >= gameBoard.rows) {
                e.setDestroy(true);
            }
        }

    }


}

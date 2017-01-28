package com.ilargia.games.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;

@Component(pools = {"Core"}, isSingleEntity = true)
public class GameBoard implements IComponent {
    public int columns;
    public int rows;

    public GameBoard(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }
}

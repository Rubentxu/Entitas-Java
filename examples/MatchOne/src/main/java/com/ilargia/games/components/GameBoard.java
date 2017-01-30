package com.ilargia.games.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Game"}, isSingleEntity = true)
public class GameBoard implements IComponent {
    public int columns;
    public int rows;

    public GameBoard(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }
}

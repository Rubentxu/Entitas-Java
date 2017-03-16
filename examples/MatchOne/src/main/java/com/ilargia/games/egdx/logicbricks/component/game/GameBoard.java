package com.ilargia.games.egdx.logicbricks.component.game;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;

@Component(pools = {"Game"}, isSingleEntity = true)
public class GameBoard implements IComponent {
    public int columns;
    public int rows;

    public GameBoard(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
    }
}

package com.ilargia.games;


import com.ilargia.games.core.game.GameContext;

public class GameBoardLogic {

    public static int getNextEmptyRow(GameContext context, int column, int row) {
        int rowBelow = row - 1;
        while (rowBelow >= 0 && EntityIndexExtension.getEntitiesWithPosition(context, column, rowBelow).size() == 0) {
            rowBelow -= 1;
        }
        return rowBelow + 1;
    }
}

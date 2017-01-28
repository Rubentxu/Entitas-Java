package com.ilargia.games;

import com.ilargia.games.entitas.Context;

public class GameBoardLogic {

    public static int getNextEmptyRow(Context context, int column, int row) {
        int rowBelow = row - 1;
        while(rowBelow >= 0 && EntityIndexExtension.getEntitiesWithPosition(context, column, rowBelow).size == 0) {
            rowBelow -= 1;
        }

        return rowBelow + 1;
    }
}

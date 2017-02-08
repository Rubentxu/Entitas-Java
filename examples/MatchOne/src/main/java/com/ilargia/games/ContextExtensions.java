package com.ilargia.games;


import com.badlogic.gdx.math.MathUtils;
import com.ilargia.games.core.GameContext;
import com.ilargia.games.core.GameEntity;

public class ContextExtensions {

    static String[] _pieces = {
            Res.Piece0,
            Res.Piece1,
            Res.Piece2,
            Res.Piece3,
            Res.Piece4,
            Res.Piece5
    };

    public static GameEntity createRandomPiece(GameContext context, int x, int y) {
        return context.createEntity()
                .setGameBoardElement(true)
                .addPosition(x, y)
                .setMovable(true)
                .setInteractive(true)
                .addAsset(_pieces[MathUtils.random(0, _pieces.length - 1)]);
    }

    public static GameEntity createBlocker(GameContext context, int x, int y) {
        return context.createEntity()
                .setGameBoardElement(true)
                .addPosition(x, y)
                .addAsset(Res.Blocker);
    }

    public static class Res {
        public static String Piece0 = "Piece0";
        public static String Piece1 = "Piece1";
        public static String Piece2 = "Piece2";
        public static String Piece3 = "Piece3";
        public static String Piece4 = "Piece4";
        public static String Piece5 = "Piece5";
        public static String Blocker = "Blocker";
    }
}

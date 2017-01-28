package com.ilargia.games;


import com.badlogic.gdx.math.MathUtils;

public class PoolExtensions {

    public static class Res {
        public static String Piece0 = "Piece0";
        public static String Piece1 = "Piece1";
        public static String Piece2 = "Piece2";
        public static String Piece3 = "Piece3";
        public static String Piece4 = "Piece4";
        public static String Piece5 = "Piece5";
        public static String Blocker = "Blocker";
    }

    static String[] _pieces = {
            Res.Piece0,
            Res.Piece1,
            Res.Piece2,
            Res.Piece3,
            Res.Piece4,
            Res.Piece5
    };

    public static Entity createRandomPiece(Pool pool, int x, int y) {
        return pool.createEntity()
                .setGameBoardElement(true)
                .addPosition(x, y)
                .setMovable(true)
                .setInteractive(true)
                .addAsset(_pieces[MathUtils.random(0, _pieces.length)]);
    }

    public static Entity createBlocker(Pool pool, int x, int y) {
        return pool.createEntity()
                .setGameBoardElement(true)
                .addPosition(x, y)
                .addAsset(Res.Blocker);
    }
}

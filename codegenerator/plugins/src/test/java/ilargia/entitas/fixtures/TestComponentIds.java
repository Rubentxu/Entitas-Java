package ilargia.entitas.fixtures;

public class TestComponentIds {

    public static final int Interactive = 0;
    public static final int Motion = 1;
    public static final int Player = 2;
    public static final int Position = 3;
    public static final int View = 4;
    public static final int Size = 5;
    public static final int totalComponents = 6;

    public static String[] componentNames() {
        return new String[]{"Interactive", "Motion", "Player", "Position",
                "Score", "View", "Size"};
    }

    public static Class[] componentTypes() {
        return new Class[]{
                ilargia.entitas.fixtures.components.View.class,
                ilargia.entitas.fixtures.components.test.Motion.class,
                ilargia.entitas.fixtures.components.test.Interactive.class,
                ilargia.entitas.fixtures.components.test.Player.class,
                ilargia.entitas.fixtures.components.test.Position.class,
                ilargia.entitas.fixtures.components.test.Size.class
        };
    }
}
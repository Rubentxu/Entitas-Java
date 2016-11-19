package com.ilargia.games.entitas.codeGenerator;

public class PruebasComponentIds {

    public static final int Position = 0;
    public static final int Movable = 1;
    public static final int totalComponents = 2;

    public static String[] componentNames() {
        return new String[]{"Position", "Movable"};
    }

    public static Class[] componentTypes() {
        return new Class[]{Position.class, Movable.class};
    }
}



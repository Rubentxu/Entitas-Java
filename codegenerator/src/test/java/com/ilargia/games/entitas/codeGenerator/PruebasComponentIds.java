package com.ilargia.games.entitas.codeGenerator;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.Movable;

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



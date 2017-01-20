package com.ilargia.games.entitas.factories;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collections {

    private static CollectionsFactory _factory;

    public Collections(CollectionsFactory factory) {
        _factory = factory;
    }

    public static List createList() {
        return _factory.createList();
    }

    public static Set createSet() {
        return _factory.createSet();
    }

    public static Map createMap() {
        return _factory.createMap();
    }
}

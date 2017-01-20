package com.ilargia.games.entitas.factories;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Collections {

    private static CollectionsFactory _factory;

    public Collections(CollectionsFactory factory) {
        _factory = factory;
    }

    public static List createList(Class<?> clazz) {
        return _factory.createList(clazz);
    }

    public static Set createSet(Class<?> clazz) {
        return _factory.createSet(clazz);
    }

    public static Map createMap(Class<?> keyClazz, Class<?> valueClazz) {
        return _factory.createMap(keyClazz, valueClazz);
    }
}

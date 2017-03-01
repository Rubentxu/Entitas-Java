package com.ilargia.games.entitas.factories;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


public class EntitasCollections {
    private static CollectionsFactories factories;

    public EntitasCollections(CollectionsFactories factories) {
        this.factories = factories;
    }

    public static <T> List<T> createList(Class<?> clazz) {
        return factories.<T>createList(clazz);
    }

    public static <T> Stack<T> createStack(Class<?> clazz) {
        return factories.<T>createStack(clazz);
    }

    public static <T> Set<T> createSet(Class<?> clazz) {
        return factories.<T>createSet(clazz);
    }

    public static <K, V> Map createMap(Class<?> keyClazz, Class<?> valueClazz) {
        return factories.<K,V>createMap(keyClazz, valueClazz);
    }

}

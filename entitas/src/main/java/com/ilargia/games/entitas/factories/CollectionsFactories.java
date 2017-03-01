package com.ilargia.games.entitas.factories;

import java.util.*;

public interface CollectionsFactories {

    default <T> List<T> createList(Class<?> clazz) {
        return new ArrayList<T>();
    }

    default <T> Stack<T> createStack(Class<?> clazz) {
        return new Stack<T>();
    }

    default <T> Set<T> createSet(Class<?> clazz) {
        return new HashSet<T>();
    }

    default <K, V> Map createMap(Class<?> keyClazz, Class<?> valueClazz) {
        return new HashMap<K,V>();
    }

}

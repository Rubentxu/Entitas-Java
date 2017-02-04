package com.ilargia.games.entitas.factories;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollectionsFactory {

    <T> List createList(Class<T> clazz);

    <T> Set createSet(Class<T> clazz);

    <K,V> Map createMap(Class<K> keyClazz, Class<V> valueClazz);

}

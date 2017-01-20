package com.ilargia.games.entitas.factories;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CollectionsFactory {

    List createList(Class<?> clazz);

    Set createSet(Class<?> clazz);

    Map createMap(Class<?> keyClazz, Class<?> valueClazz);

}

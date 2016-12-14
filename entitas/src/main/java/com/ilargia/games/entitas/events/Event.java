package com.ilargia.games.entitas.events;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class Event<T> {
    private ObjectMap<String, T> namedListeners = new ObjectMap();
    private Array<T> anonymousListeners = new Array<T>();

    public void addListener(String methodName, T namedEventHandlerMethod) {
        if (!namedListeners.containsKey(methodName))
            namedListeners.put(methodName, namedEventHandlerMethod);
    }


    public void removeListener(String methodName) {
        if (namedListeners.containsKey(methodName))
            namedListeners.remove(methodName);
    }


    public void addListener(T unnamedEventHandlerMethod) {
        anonymousListeners.add(unnamedEventHandlerMethod);
    }


    public Array<T> listeners() {
        Array<T> allListeners = new Array();
        allListeners.addAll(namedListeners.values().toArray());
        allListeners.addAll(anonymousListeners);
        return allListeners;
    }

    public void clear() {
        namedListeners.clear();
        anonymousListeners.clear();
    }

}
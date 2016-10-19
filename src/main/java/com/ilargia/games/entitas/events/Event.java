package com.ilargia.games.entitas.events;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Event<T> {
    private Map<String, T> namedListeners = new HashMap<String, T>();
    private List<T> anonymousListeners = new ArrayList<T>();

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


    public List<T> listeners() {
        List<T> allListeners = new ArrayList<T>();
        allListeners.addAll(namedListeners.values());
        allListeners.addAll(anonymousListeners);
        return allListeners;
    }

    public void clear() {
        namedListeners.clear();
        anonymousListeners.clear();
    }


}
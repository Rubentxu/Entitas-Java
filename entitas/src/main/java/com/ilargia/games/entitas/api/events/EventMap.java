package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.factories.Collections;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EventMap<K, V> {

    private Map<K, V> listeners;//ObjectArrayList

    public EventMap() {
        this.listeners = Collections.createMap(Object.class, Object.class);
    }

    public V removeListener(K key) {
        return listeners.remove(key);
    }

    public V addListener(K key, V eventHandler) {
        listeners.put(key, eventHandler);
        return eventHandler;
    }

    public Collection<V> listeners() {
        return  listeners.values();
    }

    public V getEventHandler(K key) {
        return listeners.get(key);
    }

    public void clear() {
        listeners.clear();
    }


}
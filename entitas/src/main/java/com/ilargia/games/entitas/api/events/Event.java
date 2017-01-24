package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Set;

public class Event<T> {

    private Set<T> listeners;//ObjectArrayList

    public Event() {
        this.listeners = Collections.createSet(Object.class);
    }

    public boolean removeListener(T eventHandler) {
        return listeners.remove(eventHandler);
    }

    public T addListener(T eventHandler) {
        listeners.add(eventHandler);
        return eventHandler;
    }

    public Set<T> listeners() {
        return listeners;
    }

    public <P extends Context> void clear() {
        listeners.clear();
    }


}
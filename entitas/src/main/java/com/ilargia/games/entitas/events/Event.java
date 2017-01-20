package com.ilargia.games.entitas.events;

import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.factories.Collections;
import java.util.List;

public class Event<T> {

    private List<T> listeners;//ObjectArrayList

    public Event() {
        this.listeners = Collections.createList();
    }

    public boolean removeListener(T eventHandler) {
        return listeners.remove(eventHandler);
    }

    public T addListener(T eventHandler) {
        listeners.add(eventHandler);
        return eventHandler;
    }

    public List<T> listeners() {
        return listeners;
    }

    public <P extends BasePool> void clear() {
        listeners.clear();
    }


}
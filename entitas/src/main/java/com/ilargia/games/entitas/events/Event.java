package com.ilargia.games.entitas.events;

import com.ilargia.games.entitas.BasePool;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class Event<T> {
    private ObjectArrayList<T> listeners = new ObjectArrayList<T>();

    public boolean removeListener(T eventHandler) {
        return listeners.remove(eventHandler);
    }

    public T addListener(T eventHandler) {
        listeners.add(eventHandler);
        return eventHandler;
    }

    public ObjectArrayList<T> listeners() {
        return listeners;
    }

    public <P extends BasePool> void clear() {
        listeners.clear();
    }


}
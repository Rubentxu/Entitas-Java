package com.ilargia.games.entitas.events;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.Entity;

public class Event<T> {
    private Array<T> listeners = new Array<T>();

    public boolean removeListener(T eventHandler) {
        return listeners.removeValue(eventHandler, true);
    }

    public T addListener(T eventHandler) {
        listeners.add(eventHandler);
        return eventHandler;
    }

    public Array<T> listeners() {
        return listeners;
    }

    public <P extends BasePool> void clear() {
        listeners.clear();
    }

    public <E extends Entity> void poolChanged(BasePool pool, E entity) {

    }
}
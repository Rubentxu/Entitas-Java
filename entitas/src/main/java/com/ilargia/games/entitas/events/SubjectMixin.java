package com.ilargia.games.entitas.events;

import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SubjectMixin<L> {
    private List<L> listeners = new ArrayList<>();

    public L addListener (L listener) {
        this.listeners.add(listener);
        return listener;
    }

    public void removeListener (L listener) {
        this.listeners.remove(listener);
    }

    public void notifyListeners (Consumer<? super L> algorithm) {
        this.listeners.forEach(algorithm);
    }
}

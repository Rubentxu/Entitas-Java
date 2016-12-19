package com.ilargia.games.egdx.interfaces;


public interface EventBus {
    public <L> void subscribe(L listener);
    public <E> void post(E event);
}

package com.ilargia.games.egdx.base.interfaces;


public interface EventBus {
    public <L> void subscribe(L listener);
    public <E> void post(E event);
}

package com.ilargia.games.egdx.interfaces;


import com.ilargia.games.entitas.BasePool;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.interfaces.FactoryEntity;

public interface Entitas<E extends Entity> {

    public BasePool[] allPools();

    public FactoryEntity<E> factoryEntity();

    public EventBus<E> getEventBus();

}

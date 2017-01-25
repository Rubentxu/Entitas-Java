package com.ilargia.games.egdx.base.interfaces;


import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.FactoryEntity;

public interface Entitas<E extends Entity> {

    public Context[] allPools();

    public FactoryEntity<E> factoryEntity();

    public EventBus<E> getEventBus();

}

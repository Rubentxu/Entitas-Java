package com.ilargia.games.entitas.collector;


import com.ilargia.games.entitas.api.entitas.IEntity;

import java.util.Set;

public interface ICollector<TEntity extends IEntity> {

    int getCount();
    void activate();
    void deactivate();
    void clearCollectedEntities();
    Set<TEntity> collectedEntities();
}

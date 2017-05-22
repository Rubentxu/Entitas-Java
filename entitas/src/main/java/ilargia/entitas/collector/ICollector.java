package ilargia.entitas.collector;


import ilargia.entitas.api.entitas.IEntity;

import java.util.Set;

public interface ICollector<TEntity extends IEntity> {

    int getCount();
    void activate();
    void deactivate();
    void clearCollectedEntities();
    Set<TEntity> collectedEntities();
}

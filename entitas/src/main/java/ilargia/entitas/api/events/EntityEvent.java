package ilargia.entitas.api.events;

import ilargia.entitas.api.entitas.IEntity;

@FunctionalInterface
public interface EntityEvent<TEntity extends IEntity> {
    void released(final TEntity entity);
}

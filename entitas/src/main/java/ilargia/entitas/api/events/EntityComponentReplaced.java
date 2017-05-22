package ilargia.entitas.api.events;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.entitas.IEntity;

@FunctionalInterface
public interface EntityComponentReplaced<TEntity extends IEntity> {
    void replaced(final TEntity entity, final int index, final IComponent previousComponent, final IComponent newComponent);

}

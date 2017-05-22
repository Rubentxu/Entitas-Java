package ilargia.entitas.api.events;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.entitas.IEntity;

@FunctionalInterface
public interface EntityComponentChanged<TEntity extends IEntity> {
    void changed(final TEntity entity, final int index, final IComponent component);

}
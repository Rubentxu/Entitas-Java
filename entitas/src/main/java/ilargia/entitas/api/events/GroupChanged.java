package ilargia.entitas.api.events;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.IGroup;

@FunctionalInterface
public interface GroupChanged<TEntity extends IEntity> {

    void changed(final IGroup<TEntity> group, final TEntity entity, final int index, final IComponent component);

}

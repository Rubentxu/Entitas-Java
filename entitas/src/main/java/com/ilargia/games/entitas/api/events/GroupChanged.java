package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.entitas.IEntity;
import com.ilargia.games.entitas.api.IGroup;

@FunctionalInterface
public interface GroupChanged<TEntity extends IEntity> {

    void changed(final IGroup<TEntity> group, final TEntity entity, final int index, final IComponent component);

}

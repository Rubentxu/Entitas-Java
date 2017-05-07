package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.entitas.IEntity;

@FunctionalInterface
public interface ContextEntityChanged {
    void changed(final IContext context, final IEntity entity);
}

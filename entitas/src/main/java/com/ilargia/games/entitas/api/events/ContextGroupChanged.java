package com.ilargia.games.entitas.api.events;

import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IGroup;

@FunctionalInterface
public interface ContextGroupChanged {
    void changed(final IContext context, final IGroup group);
}

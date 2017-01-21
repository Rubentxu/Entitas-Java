package com.ilargia.games.entitas.interfaces.events;

import com.ilargia.games.entitas.BaseContext;
import com.ilargia.games.entitas.Group;

@FunctionalInterface
public interface ContextGroupChanged<C extends BaseContext> {
    void groupChanged(final C context, final Group group);
}

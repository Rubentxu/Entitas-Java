package ilargia.entitas.api.events;

import ilargia.entitas.api.IContext;
import ilargia.entitas.api.IGroup;

@FunctionalInterface
public interface ContextGroupChanged {
    void changed(final IContext context, final IGroup group);
}

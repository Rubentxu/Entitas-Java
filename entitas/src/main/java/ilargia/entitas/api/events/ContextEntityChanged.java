package ilargia.entitas.api.events;

import ilargia.entitas.api.IContext;
import ilargia.entitas.api.entitas.IEntity;

@FunctionalInterface
public interface ContextEntityChanged {
    void changed(final IContext context, final IEntity entity);
}

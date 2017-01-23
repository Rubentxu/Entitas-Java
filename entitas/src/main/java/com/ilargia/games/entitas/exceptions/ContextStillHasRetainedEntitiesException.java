package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.BaseContext;
import com.ilargia.games.entitas.api.EntitasException;

public class ContextStillHasRetainedEntitiesException extends EntitasException {

    public ContextStillHasRetainedEntitiesException(BaseContext pool) {
        super("'" + pool + "' detected retained entities " +
                        "although all entities got destroyed!",
                "Did you release all entities? Try calling pool.ClearGroups() " +
                        "and system.ClearReactiveSystems() before calling " +
                        "pool.DestroyAllEntities() to avoid memory leaks.");
    }
}
package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.BasePool;

public class PoolStillHasRetainedEntitiesException extends EntitasException {

    public PoolStillHasRetainedEntitiesException(BasePool pool) {
        super("'" + pool + "' detected retained entities " +
                        "although all entities got destroyed!",
                "Did you release all entities? Try calling pool.ClearGroups() " +
                        "and systems.ClearReactiveSystems() before calling " +
                        "pool.DestroyAllEntities() to avoid memory leaks.");
    }
}
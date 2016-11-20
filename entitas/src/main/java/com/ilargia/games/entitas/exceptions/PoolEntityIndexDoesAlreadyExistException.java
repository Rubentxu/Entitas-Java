package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.BasePool;

public class PoolEntityIndexDoesAlreadyExistException extends EntitasException {

    public PoolEntityIndexDoesAlreadyExistException(BasePool pool, String name) {
        super("Cannot add EntityIndex '" + name + "' to pool '" + pool + "'!",
                "An EntityIndex with this name has already been added.");
    }
}

package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.BasePool;


public class PoolEntityIndexDoesNotExistException extends EntitasException {
    public PoolEntityIndexDoesNotExistException(BasePool pool, String name) {
        super("Cannot get EntityIndex '" + name + "' from pool '" +
                pool + "'!", "No EntityIndex with this name has been added.");
    }
}

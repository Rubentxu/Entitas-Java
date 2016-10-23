package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Pool;


public class PoolEntityIndexDoesNotExistException extends EntitasException {
    public PoolEntityIndexDoesNotExistException(Pool pool, String name) {
        super("Cannot get EntityIndex '" + name + "' from pool '" +
                pool + "'!", "No EntityIndex with this name has been added.");
    }
}

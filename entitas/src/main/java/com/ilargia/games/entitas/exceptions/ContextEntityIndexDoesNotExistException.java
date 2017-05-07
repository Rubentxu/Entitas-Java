package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.entitas.EntitasException;


public class ContextEntityIndexDoesNotExistException extends EntitasException {
    public ContextEntityIndexDoesNotExistException(Context pool, String name) {
        super("Cannot get EntityIndex '" + name + "' from pool '" +
                pool + "'!", "No EntityIndex with this name has been added.");
    }
}

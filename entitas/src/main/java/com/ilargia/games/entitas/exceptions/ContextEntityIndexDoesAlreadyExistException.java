package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.api.EntitasException;

public class ContextEntityIndexDoesAlreadyExistException extends EntitasException {

    public ContextEntityIndexDoesAlreadyExistException(Context pool, String name) {
        super("Cannot add EntityIndex '" + name + "' to pool '" + pool + "'!",
                "An EntityIndex with this name has already been added.");
    }
}

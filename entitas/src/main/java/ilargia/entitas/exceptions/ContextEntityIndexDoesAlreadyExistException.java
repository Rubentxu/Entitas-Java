package ilargia.entitas.exceptions;

import ilargia.entitas.Context;
import ilargia.entitas.api.entitas.EntitasException;

public class ContextEntityIndexDoesAlreadyExistException extends EntitasException {

    public ContextEntityIndexDoesAlreadyExistException(Context pool, String name) {
        super("Cannot add EntityIndex '" + name + "' to pool '" + pool + "'!",
                "An EntityIndex with this name has already been added.");
    }
}

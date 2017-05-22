package ilargia.entitas.exceptions;

import ilargia.entitas.Context;
import ilargia.entitas.api.entitas.EntitasException;


public class ContextEntityIndexDoesNotExistException extends EntitasException {
    public ContextEntityIndexDoesNotExistException(Context pool, String name) {
        super("Cannot get EntityIndex '" + name + "' from pool '" +
                pool + "'!", "No EntityIndex with this name has been added.");
    }
}

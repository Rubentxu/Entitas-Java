package ilargia.entitas.exceptions;

import ilargia.entitas.api.entitas.EntitasException;

public class EntityIndexException extends EntitasException {
    public EntityIndexException(String message, String hint) {
        super(message, hint);
    }
}
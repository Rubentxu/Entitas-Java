package ilargia.entitas.exceptions;

import ilargia.entitas.api.entitas.EntitasException;

public class EntityAlreadyHasComponentException extends EntitasException {

    public EntityAlreadyHasComponentException(int index, String message, String hint) {
        super(message + "\nEntity already has a component at index " + index + "!", hint);
    }

}
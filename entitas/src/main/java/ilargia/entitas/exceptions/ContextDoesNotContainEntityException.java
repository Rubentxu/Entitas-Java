package ilargia.entitas.exceptions;

import ilargia.entitas.api.entitas.EntitasException;

public class ContextDoesNotContainEntityException extends EntitasException {

    public ContextDoesNotContainEntityException(String message, String hint) {
        super(message + "\nSplashPool does not contain entity!", hint);
    }

}
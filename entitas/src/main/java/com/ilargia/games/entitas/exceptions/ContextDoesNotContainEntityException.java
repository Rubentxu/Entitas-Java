package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.api.EntitasException;

public class ContextDoesNotContainEntityException extends EntitasException {

    public ContextDoesNotContainEntityException(String message, String hint) {
        super(message + "\nSplashPool does not contain entity!", hint);
    }

}
package com.ilargia.games.entitas.exceptions;

public class ContextDoesNotContainEntityException extends EntitasException {

    public ContextDoesNotContainEntityException(String message, String hint) {
        super(message + "\nSplashPool does not contain entity!", hint);
    }

}
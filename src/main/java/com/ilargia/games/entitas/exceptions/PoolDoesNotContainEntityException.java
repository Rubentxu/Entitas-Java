package com.ilargia.games.entitas.exceptions;

public class PoolDoesNotContainEntityException extends EntitasException {

    public PoolDoesNotContainEntityException(String message, String hint) {
        super(message + "\nPool does not contain entity!", hint);
    }

}
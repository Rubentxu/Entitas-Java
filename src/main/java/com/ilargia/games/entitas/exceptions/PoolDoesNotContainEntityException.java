package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Entity;

public class PoolDoesNotContainEntityException extends EntitasException {

    public PoolDoesNotContainEntityException(String message, String hint) {
        super(message + "\nPool does not contain entity!", hint);
    }

}
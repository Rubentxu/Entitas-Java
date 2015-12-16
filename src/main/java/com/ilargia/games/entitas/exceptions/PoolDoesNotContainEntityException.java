package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Entity;

public class PoolDoesNotContainEntityException extends RuntimeException {

    public PoolDoesNotContainEntityException(Entity entity, String message) {
        super(message + "\nPool does not contain entity " + entity);
    }

}
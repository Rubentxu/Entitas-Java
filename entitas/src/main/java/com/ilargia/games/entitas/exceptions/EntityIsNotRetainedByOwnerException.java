package com.ilargia.games.entitas.exceptions;

public class EntityIsNotRetainedByOwnerException extends RuntimeException {

    public EntityIsNotRetainedByOwnerException(Object owner) {
        super("SplashEntity is not retained by owner: " + owner);
    }

}
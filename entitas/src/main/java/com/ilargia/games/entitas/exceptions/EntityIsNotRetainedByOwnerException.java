package com.ilargia.games.entitas.exceptions;

public class EntityIsNotRetainedByOwnerException extends RuntimeException {

    public EntityIsNotRetainedByOwnerException(Object owner) {
        super("Entity is not retained by owner: " + owner);
    }

}
package com.ilargia.games.entitas.exceptions;

public class EntityIsNotDestroyedException extends EntitasException {

    public EntityIsNotDestroyedException(String message) {
        super(message + "\nEntity is not destroyed yet!",
                "Did you manually call entity.Release(pool) yourself? " +
                        "If so, please don't :)");
    }

}
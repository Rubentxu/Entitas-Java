package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.api.EntitasException;

public class EntityIsNotDestroyedException extends EntitasException {

    public EntityIsNotDestroyedException(String message) {
        super(message + "\nSplashEntity is not destroyed yet!",
                "Did you manually call entity.Release(pool) yourself? " +
                        "If so, please don't :)");
    }

}
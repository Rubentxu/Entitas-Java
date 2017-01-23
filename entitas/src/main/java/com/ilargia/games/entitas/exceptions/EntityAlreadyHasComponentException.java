package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.api.EntitasException;

public class EntityAlreadyHasComponentException extends EntitasException {

    public EntityAlreadyHasComponentException(int index, String message, String hint) {
        super(message + "\nSplashEntity already has a component at index " + index + "!", hint);
    }

}
package com.ilargia.games.entitas.exceptions;

public class EntityAlreadyHasComponentException extends RuntimeException {

    public EntityAlreadyHasComponentException(String message, int index) {
        super(message + "\nEntity already has a component at index " + index);
    }

}
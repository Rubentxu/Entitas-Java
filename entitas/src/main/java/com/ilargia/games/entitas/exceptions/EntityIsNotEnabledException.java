package com.ilargia.games.entitas.exceptions;

public class EntityIsNotEnabledException extends RuntimeException {

    public EntityIsNotEnabledException(String message) {
        super(message + "\nEntity is not enabled!");
    }

}
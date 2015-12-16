package com.ilargia.games.entitas.exceptions;

public class EntityIsNotDestroyedException extends RuntimeException {

    public EntityIsNotDestroyedException(String message) {
        super(message + "\nEntity is not destroyed yet!");
    }

}
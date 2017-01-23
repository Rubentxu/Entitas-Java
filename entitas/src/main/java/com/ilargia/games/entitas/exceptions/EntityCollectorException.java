package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.api.EntitasException;

public class EntityCollectorException extends EntitasException {
    public EntityCollectorException(String message, String hint) {
        super(message, hint);
    }
}
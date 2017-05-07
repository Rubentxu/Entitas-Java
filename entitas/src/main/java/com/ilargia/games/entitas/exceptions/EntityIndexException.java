package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.api.entitas.EntitasException;

public class EntityIndexException extends EntitasException {
    public EntityIndexException(String message, String hint) {
        super(message, hint);
    }
}
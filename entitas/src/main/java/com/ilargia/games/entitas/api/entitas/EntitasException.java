package com.ilargia.games.entitas.api.entitas;

public class EntitasException extends RuntimeException {

    public EntitasException(String message, String hint) {
        new Exception(hint != null ? (message + "\n" + hint) : message);
    }
}

package com.ilargia.games.entitas.exceptions;

public class EntitasException extends RuntimeException{

    public EntitasException(String message, String hint) {
        new Exception(hint != null ? (message + "\n" + hint) : message);
    }
}

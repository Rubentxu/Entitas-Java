package com.ilargia.games.entitas.exceptions;

public class EntitasException extends Exception{

    public EntitasException(String message, String hint) {
        new Exception(hint != null ? (message + "\n" + hint) : message);
    }
}

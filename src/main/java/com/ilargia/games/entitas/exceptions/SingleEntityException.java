package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.interfaces.IMatcher;

public class SingleEntityException extends RuntimeException {

    public SingleEntityException(IMatcher matcher) {
        super("Multiple entities exist matching " + matcher);
    }

}
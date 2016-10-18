package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.interfaces.IMatcher;


public class MatcherException extends RuntimeException {

    public MatcherException(IMatcher matcher) {
        super("length matcher index must contain at least one, and has " + matcher.getindices().length);
    }

}
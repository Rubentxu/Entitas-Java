package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.interfaces.IMatcher;


public class MatcherException extends RuntimeException {

    public MatcherException(IMatcher matcher) {
        super("matcher.indices.Length must be 1 but was " + matcher.getindices().size());
    }

}
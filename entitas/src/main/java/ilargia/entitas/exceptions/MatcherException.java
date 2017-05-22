package ilargia.entitas.exceptions;

import ilargia.entitas.api.matcher.IMatcher;


public class MatcherException extends RuntimeException {

    public MatcherException(IMatcher matcher) {
        super("length matcher index must contain at least one, and has " + matcher.getIndices().length);
    }

}
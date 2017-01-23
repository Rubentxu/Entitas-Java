package com.ilargia.games.entitas.api.matcher;

public interface IAnyOfMatcher extends ICompoundMatcher {

    INoneOfMatcher noneOf(int... indices);

    INoneOfMatcher noneOf(IMatcher... matchers);

}
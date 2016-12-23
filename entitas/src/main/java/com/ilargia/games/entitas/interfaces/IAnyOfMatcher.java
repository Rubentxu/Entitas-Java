package com.ilargia.games.entitas.interfaces;

public interface IAnyOfMatcher extends ICompoundMatcher {

    INoneOfMatcher noneOf(int... indices);

    INoneOfMatcher noneOf(IMatcher... matchers);

}
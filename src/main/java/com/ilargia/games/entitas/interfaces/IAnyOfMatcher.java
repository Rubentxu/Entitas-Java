package com.ilargia.games.entitas.interfaces;

public interface IAnyOfMatcher extends ICompoundMatcher {

    INoneOfMatcher noneOf(Integer... indices);

    INoneOfMatcher noneOf(IMatcher... matchers);

}
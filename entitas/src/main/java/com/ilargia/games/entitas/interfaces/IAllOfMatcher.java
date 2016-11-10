package com.ilargia.games.entitas.interfaces;

public interface IAllOfMatcher extends ICompoundMatcher {

    IAnyOfMatcher anyOf(Integer... indices);

    IAnyOfMatcher anyOf(IMatcher... matchers);

    INoneOfMatcher noneOf(Integer... indices);

    INoneOfMatcher noneOf(IMatcher... matchers);

}
package com.ilargia.games.entitas.interfaces;

public interface IAllOfMatcher extends ICompoundMatcher {

    IAnyOfMatcher anyOf(int... indices);

    IAnyOfMatcher anyOf(IMatcher... matchers);

    INoneOfMatcher noneOf(int... indices);

    INoneOfMatcher noneOf(IMatcher... matchers);

}
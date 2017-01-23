package com.ilargia.games.entitas.api.matcher;

public interface ICompoundMatcher extends IMatcher {

    int[] getAllOfIndices();

    int[] getAnyOfIndices();

    int[] getNoneOfIndices();

}
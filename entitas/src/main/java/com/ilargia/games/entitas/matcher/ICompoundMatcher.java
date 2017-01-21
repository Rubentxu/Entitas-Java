package com.ilargia.games.entitas.matcher;

public interface ICompoundMatcher extends IMatcher {

    int[] getAllOfIndices();

    int[] getAnyOfIndices();

    int[] getNoneOfIndices();

}
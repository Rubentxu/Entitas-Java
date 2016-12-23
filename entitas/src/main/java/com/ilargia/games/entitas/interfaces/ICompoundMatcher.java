package com.ilargia.games.entitas.interfaces;

public interface ICompoundMatcher extends IMatcher {

    int[] getAllOfIndices();

    int[] getAnyOfIndices();

    int[] getNoneOfIndices();

}
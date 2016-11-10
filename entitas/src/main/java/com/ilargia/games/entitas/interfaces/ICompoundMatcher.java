package com.ilargia.games.entitas.interfaces;

public interface ICompoundMatcher extends IMatcher {

    Integer[] getAllOfIndices();

    Integer[] getAnyOfIndices();

    Integer[] getNoneOfIndices();

}
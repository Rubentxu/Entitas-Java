package com.ilargia.games.entitas.interfaces;

import java.util.List;

public interface ICompoundMatcher extends IMatcher {

    List<Integer> getallOfIndices();

    List<Integer> getanyOfIndices();

    List<Integer> getnoneOfIndices();

}
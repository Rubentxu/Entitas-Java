package com.ilargia.games.entitas.interfaces;

import com.badlogic.gdx.utils.IntArray;

import java.util.List;

public interface ICompoundMatcher extends IMatcher {

    int[] getallOfIndices();

    int[] getanyOfIndices();

    int[] getnoneOfIndices();

}
package com.ilargia.games.entitas.interfaces;

import com.badlogic.gdx.utils.IntArray;

import java.util.List;

public interface ICompoundMatcher extends IMatcher {

    IntArray getallOfIndices();

    IntArray getanyOfIndices();

    IntArray getnoneOfIndices();

}
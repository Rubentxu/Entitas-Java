package com.ilargia.games.entitas.interfaces;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.ilargia.games.entitas.Entity;

import java.util.List;

public interface IMatcher {

    int[] getIndices();

    boolean matches(Entity entity);

}
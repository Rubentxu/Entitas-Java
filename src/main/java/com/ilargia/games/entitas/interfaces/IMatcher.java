package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

public interface IMatcher {

    int[] getIndices();

    boolean matches(Entity entity);

}
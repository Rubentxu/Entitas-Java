package com.ilargia.games.entitas.matcher;

import com.ilargia.games.entitas.Entity;

public interface IMatcher {

    int[] getIndices();

    boolean matches(Entity entity);

}
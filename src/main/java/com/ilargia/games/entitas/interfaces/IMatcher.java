package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;

public interface IMatcher {

    Integer[] getIndices();

    boolean matches(Entity entity);

}
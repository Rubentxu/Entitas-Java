package com.ilargia.games.entitas.api.matcher;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IEntity;

public interface IMatcher<TEntity extends IEntity> {

    int[] getIndices();

    boolean matches(TEntity entity);

}
package ilargia.entitas.api.matcher;

import ilargia.entitas.api.entitas.IEntity;

public interface IMatcher<TEntity extends IEntity> {

    int[] getIndices();

    boolean matches(TEntity entity);

}
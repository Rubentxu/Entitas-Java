package ilargia.entitas.api.matcher;

import ilargia.entitas.api.entitas.IEntity;

public interface ICompoundMatcher<TEntity extends IEntity> extends IMatcher<TEntity> {

    int[] getAllOfIndices();

    int[] getAnyOfIndices();

    int[] getNoneOfIndices();

}
package ilargia.entitas.api.matcher;

import ilargia.entitas.api.entitas.IEntity;

public interface IAllOfMatcher<TEntity extends IEntity> extends IAnyOfMatcher<TEntity> {

    IAnyOfMatcher<TEntity> anyOf(int... indices);

    IAnyOfMatcher<TEntity> anyOf(IMatcher<TEntity>... matchers);

}
package com.ilargia.games.entitas.api.matcher;

import com.ilargia.games.entitas.api.IEntity;

public interface IAllOfMatcher<TEntity extends IEntity> extends IAnyOfMatcher<TEntity> {

    IAnyOfMatcher<TEntity> anyOf(int... indices);

    IAnyOfMatcher<TEntity> anyOf(IMatcher<TEntity>... matchers);

}
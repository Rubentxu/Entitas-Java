package com.ilargia.games.entitas.api.matcher;

import com.ilargia.games.entitas.api.IEntity;

public interface IAnyOfMatcher<TEntity extends IEntity> extends INoneOfMatcher<TEntity> {

    INoneOfMatcher<TEntity> noneOf(int... indices);

    INoneOfMatcher<TEntity> noneOf(IMatcher<TEntity>... matchers);

}
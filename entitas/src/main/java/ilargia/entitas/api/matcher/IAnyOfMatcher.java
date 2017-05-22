package ilargia.entitas.api.matcher;

import ilargia.entitas.api.entitas.IEntity;

public interface IAnyOfMatcher<TEntity extends IEntity> extends INoneOfMatcher<TEntity> {

    INoneOfMatcher<TEntity> noneOf(int... indices);

    INoneOfMatcher<TEntity> noneOf(IMatcher<TEntity>... matchers);

}
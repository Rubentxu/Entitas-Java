package com.ilargia.games.entitas.collector;


import com.ilargia.games.entitas.api.entitas.IEntity;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.group.GroupEvent;

public class TriggerOnEventMatcher {

    public static <TEntity  extends IEntity> TriggerOnEvent<TEntity> added(IMatcher<TEntity> matcher) {
        return new TriggerOnEvent(matcher, GroupEvent.Added);
    }

    public static <TEntity  extends IEntity> TriggerOnEvent<TEntity> removed(IMatcher<TEntity> matcher) {
        return new TriggerOnEvent<TEntity>(matcher, GroupEvent.Removed);
    }

    public static <TEntity  extends IEntity> TriggerOnEvent<TEntity> AddedOrRemoved(IMatcher<TEntity> matcher) {
        return new TriggerOnEvent<TEntity>(matcher, GroupEvent.AddedOrRemoved);
    }
}
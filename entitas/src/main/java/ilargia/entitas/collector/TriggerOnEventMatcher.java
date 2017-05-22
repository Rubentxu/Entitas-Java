package ilargia.entitas.collector;


import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.matcher.IMatcher;
import ilargia.entitas.group.GroupEvent;

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
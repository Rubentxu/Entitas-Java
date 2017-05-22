package ilargia.entitas.collector;


import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.matcher.IMatcher;
import ilargia.entitas.group.GroupEvent;

public final class TriggerOnEvent<TEntity extends IEntity> {
    public IMatcher<TEntity> matcher;
    public GroupEvent groupEvent;

    public TriggerOnEvent(IMatcher matcher, GroupEvent groupEvent) {
        this.matcher = matcher;
        this.groupEvent = groupEvent;
    }

}
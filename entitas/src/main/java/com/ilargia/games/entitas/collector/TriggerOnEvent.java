package com.ilargia.games.entitas.collector;


import com.ilargia.games.entitas.api.entitas.IEntity;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.group.GroupEvent;

public final class TriggerOnEvent<TEntity extends IEntity> {
    public IMatcher<TEntity> matcher;
    public GroupEvent groupEvent;

    public TriggerOnEvent(IMatcher matcher, GroupEvent groupEvent) {
        this.matcher = matcher;
        this.groupEvent = groupEvent;
    }

}
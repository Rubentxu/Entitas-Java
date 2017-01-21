package com.ilargia.games.entitas.matcher;


import com.ilargia.games.entitas.events.GroupEvent;

public final class TriggerOnEvent {
    public IMatcher trigger;
    public GroupEvent eventType;

    public TriggerOnEvent() {
    }

    public TriggerOnEvent(IMatcher trigger, GroupEvent eventType) {
        this.trigger = trigger;
        this.eventType = eventType;
    }

    public TriggerOnEvent clone() {
        TriggerOnEvent varCopy = new TriggerOnEvent();

        varCopy.trigger = this.trigger;
        varCopy.eventType = this.eventType;

        return varCopy;
    }
}
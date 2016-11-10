package com.ilargia.games.entitas.matcher;


import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.interfaces.IMatcher;

public final class TriggerOnEvent {
    public IMatcher trigger;
    public GroupEventType eventType;

    public TriggerOnEvent() {
    }

    public TriggerOnEvent(IMatcher trigger, GroupEventType eventType) {
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
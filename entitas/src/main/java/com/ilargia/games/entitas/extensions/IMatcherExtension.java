package com.ilargia.games.entitas.extensions;


import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.interfaces.IMatcher;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;

public final class IMatcherExtension {

    public static TriggerOnEvent onEntityAdded(IMatcher matcher) {
        return new TriggerOnEvent(matcher, GroupEventType.OnEntityAdded);

    }


    public static TriggerOnEvent onEntityRemoved(IMatcher matcher) {
        return new TriggerOnEvent(matcher, GroupEventType.OnEntityRemoved);

    }


    public static TriggerOnEvent onEntityAddedOrRemoved(IMatcher matcher) {
        return new TriggerOnEvent(matcher, GroupEventType.OnEntityAddedOrRemoved);

    }

}
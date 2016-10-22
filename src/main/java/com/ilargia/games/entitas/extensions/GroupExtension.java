package com.ilargia.games.entitas.extensions;

import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.events.GroupEventType;

public final class GroupExtension {

    public static GroupObserver createObserver(Group group) {
        return createObserver(group, GroupEventType.OnEntityAdded);

    }


    public static GroupObserver createObserver(Group group, GroupEventType eventType) {
        return new GroupObserver(group, eventType);

    }

}
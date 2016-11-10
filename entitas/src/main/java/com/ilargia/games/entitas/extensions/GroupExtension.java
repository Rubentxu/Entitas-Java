package com.ilargia.games.entitas.extensions;

import com.ilargia.games.entitas.EntityCollector;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.events.GroupEventType;

public final class GroupExtension {

    public static EntityCollector createObserver(Group group) {
        return createObserver(group, GroupEventType.OnEntityAdded);

    }

    public static EntityCollector createObserver(Group group, GroupEventType eventType) {
        return new EntityCollector(group, eventType);

    }

}
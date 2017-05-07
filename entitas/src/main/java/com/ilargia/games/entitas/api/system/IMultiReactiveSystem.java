package com.ilargia.games.entitas.api.system;

import com.ilargia.games.entitas.matcher.TriggerOnEvent;

public interface IMultiReactiveSystem extends IReactiveExecuteSystem {
    TriggerOnEvent[] getTriggers();
}
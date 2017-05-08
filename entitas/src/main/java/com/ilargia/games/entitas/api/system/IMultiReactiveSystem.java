package com.ilargia.games.entitas.api.system;

import com.ilargia.games.entitas.collector.TriggerOnEvent;

public interface IMultiReactiveSystem extends IReactiveExecuteSystem {
    TriggerOnEvent[] getTriggers();
}
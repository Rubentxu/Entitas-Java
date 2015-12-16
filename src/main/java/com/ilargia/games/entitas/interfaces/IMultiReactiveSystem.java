package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.matcher.TriggerOnEvent;

public interface IMultiReactiveSystem extends IReactiveExecuteSystem {
    TriggerOnEvent[] getTriggers();
}
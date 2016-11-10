package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.matcher.TriggerOnEvent;

public interface IReactiveSystem extends IReactiveExecuteSystem {
    TriggerOnEvent getTrigger();
}
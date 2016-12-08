package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;

public interface IReactiveSystem<E extends Entity> extends IReactiveExecuteSystem<E> {
    TriggerOnEvent getTrigger();
}
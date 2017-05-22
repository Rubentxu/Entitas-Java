package ilargia.entitas.api.system;

import ilargia.entitas.collector.TriggerOnEvent;

public interface IMultiReactiveSystem extends IReactiveExecuteSystem {
    TriggerOnEvent[] getTriggers();
}
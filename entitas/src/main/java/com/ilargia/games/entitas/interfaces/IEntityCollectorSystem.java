package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.collector.Collector;

public interface IEntityCollectorSystem extends IReactiveExecuteSystem {

    Collector getEntityCollector();

}

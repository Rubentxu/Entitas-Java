package com.ilargia.games.entitas.api.system;

public interface IReactiveSystem extends IExecuteSystem {

    void activate();

    void deactivate();

    void clear();

}
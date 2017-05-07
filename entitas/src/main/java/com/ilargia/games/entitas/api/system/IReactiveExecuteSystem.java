package com.ilargia.games.entitas.api.system;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.system.ISystem;

import java.util.List;

public interface IReactiveExecuteSystem<E extends Entity> extends ISystem {
    void execute(List<Entity> entities);
}
package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import java.util.List;

public interface IReactiveExecuteSystem<E extends Entity> extends ISystem {
    void execute(List<Entity> entities);
}
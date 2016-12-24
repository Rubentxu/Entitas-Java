package com.ilargia.games.entitas.interfaces;

import com.ilargia.games.entitas.Entity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public interface IReactiveExecuteSystem<E extends Entity> extends ISystem {
    void execute(ObjectArrayList<Entity> entities);
}
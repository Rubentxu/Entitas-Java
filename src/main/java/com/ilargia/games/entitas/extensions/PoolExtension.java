package com.ilargia.games.entitas.extensions;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Pool;
import com.ilargia.games.entitas.ReactiveSystem;
import com.ilargia.games.entitas.interfaces.IMatcher;
import com.ilargia.games.entitas.interfaces.IMultiReactiveSystem;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISystem;

public final class PoolExtension {

    public static Entity[] getEntities(Pool pool, IMatcher matcher) {
        return pool.getGroup(matcher).getEntities();

    }


    public static ISystem createSystem(Pool pool, Class<? extends ISystem> systemType) {
        try {
            ISystem system = systemType.newInstance();
            return PoolExtension.createSystem(pool, system);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public static ISystem createSystem(Pool pool, ISystem system) {
        setPool(system, pool);
        IReactiveSystem reactiveSystem = (IReactiveSystem) ((system instanceof IReactiveSystem) ? system : null);
        if (reactiveSystem != null) {
            return new ReactiveSystem(pool, reactiveSystem);
        }
        IMultiReactiveSystem multiReactiveSystem = (IMultiReactiveSystem) ((system instanceof IMultiReactiveSystem) ? system : null);
        if (multiReactiveSystem != null) {
            return new ReactiveSystem(pool, multiReactiveSystem);
        }

        return system;

    }


    private static void setPool(ISystem system, Pool pool) {
        ISetPool poolSystem = (ISetPool) ((system instanceof ISetPool) ? system : null);
        if (poolSystem != null) {
            poolSystem.setPool(pool);
        }

    }

}
package com.ilargia.games.entitas.extensions;

import com.ilargia.games.entitas.*;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.exceptions.EntitasException;
import com.ilargia.games.entitas.interfaces.*;

public final class PoolExtension {

    public static Entity[] getEntities(Pool pool, IMatcher matcher) {
        return pool.getGroup(matcher).getEntities();

    }


    public static ISystem createSystem(Pool pool, ISystem system) {
        return createSystem(pool, system, Pools.getSharedInstance());
    }

    public static ISystem createSystem(Pool pool, ISystem system, Pools pools) {
        setPool(system, pool);
        setPools(system, pools);
        return system;
    }

    public static ISystem createSystem(Pool pool, IReactiveExecuteSystem system) {
        return createSystem(pool, system, Pools.getSharedInstance());
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

    public static ISystem CreateSystem(Pool pool, IReactiveExecuteSystem system, Pools pools) {
        setPool(system, pool);
        setPools(system, pools);

        IReactiveSystem reactiveSystem = (IReactiveSystem) ((system instanceof IReactiveSystem) ? system : null);
        if(reactiveSystem != null) {
            return new ReactiveSystem(pool, reactiveSystem);
        }
        IMultiReactiveSystem multiReactiveSystem = (IMultiReactiveSystem) ((system instanceof IMultiReactiveSystem) ? system : null);
        if(multiReactiveSystem != null) {
            return new ReactiveSystem(pool, multiReactiveSystem);
        }
        IEntityCollectorSystem entityCollectorSystem = (IEntityCollectorSystem) ((system instanceof IEntityCollectorSystem) ? system : null);
        if(entityCollectorSystem != null) {
            return new ReactiveSystem(entityCollectorSystem);
        }

        throw new EntitasException( "Could not create ReactiveSystem for " + system + "!", "The system has to implement IReactiveSystem, " +
                        "IMultiReactiveSystem or IEntityCollectorSystem.");
    }

    public static ISystem CreateSystem(Pools pools, ISystem system) {
        setPools(system, pools);
        return system;
    }


    public static ISystem CreateSystem(Pools pools, IReactiveExecuteSystem system) {
        setPools(system, pools);

        IEntityCollectorSystem entityCollectorSystem = (IEntityCollectorSystem) ((system instanceof IEntityCollectorSystem) ? system : null);
        if(entityCollectorSystem != null) {
            return new ReactiveSystem(entityCollectorSystem);
        }

        throw new EntitasException("Could not create ReactiveSystem for " + system + "!","Only IEntityCollectorSystem is supported for " +
                        "pools.CreateSystem(system).");
    }


    private static void setPool(ISystem system, Pool pool) {
        ISetPool poolSystem = (ISetPool) ((system instanceof ISetPool) ? system : null);
        if (poolSystem != null) {
            poolSystem.setPool(pool);
        }

    }

    public static void setPools(ISystem system, Pools pools) {
        ISetPools poolsSystem = (ISetPools) ((system instanceof ISetPool) ? system : null);
        if(poolsSystem != null) {
            poolsSystem.setPools(pools);
        }
    }


    public static EntityCollector createEntityCollector(Pool[] pools, IMatcher matcher) {
        return createEntityCollector(pools, matcher, GroupEventType.OnEntityAdded);
    }

    public static EntityCollector createEntityCollector(Pool[] pools, IMatcher matcher, GroupEventType eventType ) {
        Group[] groups = new Group[pools.length];
        GroupEventType[] eventTypes = new GroupEventType[pools.length];

        for (int i = 0; i < pools.length; i++) {
            groups[i] = pools[i].getGroup(matcher);
            eventTypes[i] = eventType;
        }

        return new EntityCollector(groups, eventTypes);
    }

    public static Entity cloneEntity(Pool pool, Entity entity, boolean replaceExisting, Integer[] indices) {
        Entity target = pool.createEntity();
        try {
            EntityExtension.copyTo(entity, target, replaceExisting, indices);
        } catch (Exception e) {

        }
        return target;
    }


}
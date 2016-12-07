package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.exceptions.EntitasException;
import com.ilargia.games.entitas.interfaces.*;

public class Systems implements IInitializeSystem, IExecuteSystem, ICleanupSystem, ITearDownSystem {

    protected Array<IInitializeSystem> _initializeSystems;
    protected Array<IExecuteSystem> _executeSystems;
    protected Array<ICleanupSystem> _cleanupSystems;
    protected Array<ITearDownSystem> _tearDownSystems;

    public Systems() {
        _initializeSystems = new Array<IInitializeSystem>();
        _executeSystems = new Array<IExecuteSystem>();
        _cleanupSystems = new Array();
        _tearDownSystems = new Array();
    }


    private Systems add(ISystem system) {
        if(system instanceof ReactiveSystem) {
            addSystem(((ReactiveSystem) system).getSubsystem());
            addSystem(system);
        } else {
            addSystem(system);
        }

        return this;
    }

    private void addSystem(ISystem system) {
        if(system instanceof IInitializeSystem) _initializeSystems.add((IInitializeSystem) system);
        if(system instanceof IExecuteSystem) _executeSystems.add((IExecuteSystem) system);
        if(system instanceof ICleanupSystem) _cleanupSystems.add((ICleanupSystem) system);
        if(system instanceof ITearDownSystem) _tearDownSystems.add((ITearDownSystem) system);

    }

    public void initialize() {
        for (IInitializeSystem iSystem : _initializeSystems) {
            iSystem.initialize();
        }
    }

    public void execute() {
        for (IExecuteSystem eSystem : _executeSystems) {
            eSystem.execute();
        }
    }

    public void cleanup() {
        for (ICleanupSystem clSystem : _cleanupSystems) {
            clSystem.cleanup();
        }
    }

    public void tearDown() {
        for (ITearDownSystem tSystem : _tearDownSystems) {
            tSystem.tearDown();
        }
    }

    public void activateReactiveSystems() {
        for (int i = 0; i < _executeSystems.size; i++) {
            ReactiveSystem reactiveSystem = (ReactiveSystem) ((_executeSystems.get(i) instanceof ReactiveSystem) ? _executeSystems.get(i) : null);
            if (reactiveSystem != null) {
                reactiveSystem.activate();
            }

            Systems nestedSystems = (Systems) ((_executeSystems.get(i) instanceof Systems) ? _executeSystems.get(i) : null);
            if (nestedSystems != null) {
                nestedSystems.activateReactiveSystems();
            }
        }
    }

    public void deactivateReactiveSystems() {
        for (int i = 0; i < _executeSystems.size; i++) {
            ReactiveSystem reactiveSystem = (ReactiveSystem) ((_executeSystems.get(i) instanceof ReactiveSystem) ? _executeSystems.get(i) : null);
            if (reactiveSystem != null) {
                reactiveSystem.deactivate();
            }

            Systems nestedSystems = (Systems) ((_executeSystems.get(i) instanceof Systems) ? _executeSystems.get(i) : null);
            if (nestedSystems != null) {
                nestedSystems.deactivateReactiveSystems();
            }
        }
    }

    public void clearReactiveSystems() {
        for (int i = 0; i < _executeSystems.size; i++) {
            ReactiveSystem reactiveSystem = (ReactiveSystem) ((_executeSystems.get(i) instanceof ReactiveSystem) ? _executeSystems.get(i) : null);
            if (reactiveSystem != null) {
                reactiveSystem.clear();
            }

            Systems nestedSystems = (Systems) ((_executeSystems.get(i) instanceof Systems) ? _executeSystems.get(i) : null);
            if (nestedSystems != null) {
                nestedSystems.clearReactiveSystems();
            }
        }
    }


    public <P> Systems addSystem(BasePool pool, ISystem system, P pools) {
        setPool(system, pool);
        setPools(system, pools);
        add(system);
        return this;
    }

    public <P> Systems addSystem(BasePool pool, IReactiveExecuteSystem system, P pools) {
        setPool(system, pool);
        setPools(system, pools);

        IReactiveSystem reactiveSystem = (IReactiveSystem) ((system instanceof IReactiveSystem) ? system : null);
        if (reactiveSystem != null) {
            add(new ReactiveSystem(pool, reactiveSystem));
            return this;
        }
        IMultiReactiveSystem multiReactiveSystem = (IMultiReactiveSystem) ((system instanceof IMultiReactiveSystem) ? system : null);
        if (multiReactiveSystem != null) {
            add(new ReactiveSystem(pool, multiReactiveSystem));
            return this;
        }
        IEntityCollectorSystem entityCollectorSystem = (IEntityCollectorSystem) ((system instanceof IEntityCollectorSystem) ? system : null);
        if (entityCollectorSystem != null) {
            add(new ReactiveSystem(entityCollectorSystem));
            return this;
        }

        throw new EntitasException("Could not create ReactiveSystem for " + system + "!", "The system has to implement IReactiveSystem, " +
                "IMultiReactiveSystem or IEntityCollectorSystem.");
    }

    public Systems addSystem(BasePool pool, ISystem system) {
        setPool(system, pool);
        add(system);
        return this;
    }


    public Systems addSystem(BasePool pool, IReactiveExecuteSystem system) {
        setPool(system, pool);

        IEntityCollectorSystem entityCollectorSystem = (IEntityCollectorSystem) ((system instanceof IEntityCollectorSystem) ? system : null);
        if (entityCollectorSystem != null) {
            add(new ReactiveSystem(entityCollectorSystem));
            return this;
        }
        throw new EntitasException("Could not create ReactiveSystem for " + system + "!", "Only IEntityCollectorSystem is supported for " +
                "pools.createSystem(system).");
    }


    private <P> void setPool(ISystem system, P pool) {
        ISetPool poolSystem = (ISetPool) ((system instanceof ISetPool) ? system : null);
        if (poolSystem != null) {
            poolSystem.setPool(pool);
        }

    }

    public <P> void setPools(ISystem system, P pools) {
        ISetPools poolsSystem = (ISetPools) ((system instanceof ISetPools) ? system : null);
        if (poolsSystem != null) {
            poolsSystem.setPools(pools);
        }
    }


}
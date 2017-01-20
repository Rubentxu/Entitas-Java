package com.ilargia.games.entitas;

import com.ilargia.games.entitas.exceptions.EntitasException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.interfaces.*;
import java.util.List;


public class Systems implements IInitializeSystem, IExecuteSystem, ICleanupSystem, ITearDownSystem {

    private List<IInitializeSystem> _initializeSystems; // ObjectArrayList
    private List<IExecuteSystem> _executeSystems;
    private List<ICleanupSystem> _cleanupSystems;
    private List<IRenderSystem> _renderSystems;
    private List<ITearDownSystem> _tearDownSystems;

    public Systems() {
        _initializeSystems = Collections.createList(ISystem.class);
        _executeSystems = Collections.createList(ISystem.class);
        _cleanupSystems = Collections.createList(ISystem.class);
        _renderSystems = Collections.createList(ISystem.class);
        _tearDownSystems = Collections.createList(ISystem.class);
    }


    private Systems add(ISystem system) {
        if (system instanceof ReactiveSystem) {
            addSystem(((ReactiveSystem) system).getSubsystem());
            addSystem(system);
        } else {
            addSystem(system);
        }

        return this;
    }

    private void addSystem(ISystem system) {
        if (system instanceof IInitializeSystem) _initializeSystems.add((IInitializeSystem) system);
        if (system instanceof IExecuteSystem) _executeSystems.add((IExecuteSystem) system);
        if (system instanceof ICleanupSystem) _cleanupSystems.add((ICleanupSystem) system);
        if (system instanceof IRenderSystem) _renderSystems.add((IRenderSystem) system);
        if (system instanceof ITearDownSystem) _tearDownSystems.add((ITearDownSystem) system);

    }

    public void initialize() {
        for (IInitializeSystem iSystem : _initializeSystems) {
            iSystem.initialize();
        }
    }

    public void execute(float deltaTime) {
        for (IExecuteSystem eSystem : _executeSystems) {
            eSystem.execute(deltaTime);
        }
    }

    public void cleanup() {
        for (ICleanupSystem clSystem : _cleanupSystems) {
            clSystem.cleanup();
        }
    }

    public void render() {
        for (IRenderSystem renderSystem : _renderSystems) {
            renderSystem.render();
        }
    }

    public void tearDown() {
        for (ITearDownSystem tSystem : _tearDownSystems) {
            tSystem.tearDown();
        }
    }

    public void activateReactiveSystems() {
        for (int i = 0; i < _executeSystems.size(); i++) {
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
        for (int i = 0; i < _executeSystems.size(); i++) {
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
        for (int i = 0; i < _executeSystems.size(); i++) {
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

    public void clearSystems() {
        _initializeSystems.clear();
        _executeSystems.clear();
        _cleanupSystems.clear();
        _renderSystems.clear();
        _tearDownSystems.clear();

    }


}
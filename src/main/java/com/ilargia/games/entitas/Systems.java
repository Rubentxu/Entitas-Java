package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
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


    public Systems add(ISystem system) {
        ReactiveSystem reactiveSystem = (ReactiveSystem) ((system instanceof ReactiveSystem) ? system : null);
        IReactiveExecuteSystem tempVar = reactiveSystem.getSubsystem();

        IInitializeSystem initializeSystem = reactiveSystem != null
                ? (IInitializeSystem) ((tempVar instanceof IInitializeSystem) ? tempVar : null)
                : (IInitializeSystem) ((system instanceof IInitializeSystem) ? system : null);

        if (initializeSystem != null) {
            _initializeSystems.add(initializeSystem);
        }

        IExecuteSystem executeSystem = (IExecuteSystem) ((system instanceof IExecuteSystem) ? system : null);
        if (executeSystem != null) {
            _executeSystems.add(executeSystem);
        }

        ICleanupSystem cleanupSystem = reactiveSystem != null
                ? (ICleanupSystem) reactiveSystem.getSubsystem()
                : (ICleanupSystem) system;

        if (cleanupSystem != null) {
            _cleanupSystems.add(cleanupSystem);
        }

        ITearDownSystem tearDownSystem = reactiveSystem != null
                ? (ITearDownSystem) reactiveSystem.getSubsystem()
                : (ITearDownSystem) system;

        if (tearDownSystem != null) {
            _tearDownSystems.add(tearDownSystem);
        }
        return this;
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

}
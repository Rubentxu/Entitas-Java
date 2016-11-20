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

        if(system instanceof ReactiveSystem) {
            addSystem(((ReactiveSystem) system).getSubsystem());
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

}
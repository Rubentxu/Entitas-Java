package com.ilargia.games.entitas;

import com.ilargia.games.entitas.interfaces.IExecuteSystem;
import com.ilargia.games.entitas.interfaces.IInitializeSystem;
import com.ilargia.games.entitas.interfaces.IReactiveExecuteSystem;
import com.ilargia.games.entitas.interfaces.ISystem;

import java.util.ArrayList;

public class Systems implements IInitializeSystem, IExecuteSystem {

    protected ArrayList<IInitializeSystem> _initializeSystems;
    protected ArrayList<IExecuteSystem> _executeSystems;

    public Systems() {
        _initializeSystems = new ArrayList<IInitializeSystem>();
        _executeSystems = new ArrayList<IExecuteSystem>();
    }

    public <T> Systems add(Class<T> systemType) throws IllegalAccessException, InstantiationException {
        return add((ISystem) systemType.newInstance());
    }

    public Systems add(ISystem system) {
        ReactiveSystem reactiveSystem = (ReactiveSystem) ((system instanceof ReactiveSystem) ? system : null);
        IReactiveExecuteSystem tempVar = reactiveSystem.getsubsystem();

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

    public void activateReactiveSystems() {
        for (int i = 0, exeSysCount = _executeSystems.size(); i < exeSysCount; i++) {
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
        for (int i = 0, exeSysCount = _executeSystems.size(); i < exeSysCount; i++) {
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
        for (int i = 0, exeSysCount = _executeSystems.size(); i < exeSysCount; i++) {
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
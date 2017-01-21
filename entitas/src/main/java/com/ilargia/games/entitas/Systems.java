package com.ilargia.games.entitas;

import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.interfaces.*;
import java.util.List;


public class Systems implements ISystem.IInitializeSystem, ISystem.IExecuteSystem, ISystem.ICleanupSystem, ISystem.ITearDownSystem {

    private final EventBus _eventBus;
    private List<IInitializeSystem> _initializeSystems; // ObjectArrayList
    private List<IExecuteSystem> _executeSystems;
    private List<ICleanupSystem> _cleanupSystems;
    private List<IRenderSystem> _renderSystems;
    private List<ITearDownSystem> _tearDownSystems;

    public Systems(EventBus eventBus) {
        _initializeSystems = Collections.createList(ISystem.class);
        _executeSystems = Collections.createList(ISystem.class);
        _cleanupSystems = Collections.createList(ISystem.class);
        _renderSystems = Collections.createList(ISystem.class);
        _tearDownSystems = Collections.createList(ISystem.class);
        _eventBus = eventBus;
    }

    public Systems addSystem(ISystem system) {
        if(system != null) {
            if (system instanceof IInitializeSystem) _initializeSystems.add((IInitializeSystem) system);
            if (system instanceof IExecuteSystem) _executeSystems.add((IExecuteSystem) system);
            if (system instanceof ICleanupSystem) _cleanupSystems.add((ICleanupSystem) system);
            if (system instanceof IRenderSystem) _renderSystems.add((IRenderSystem) system);
            if (system instanceof ITearDownSystem) _tearDownSystems.add((ITearDownSystem) system);
        }
        return this;
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

    public void clearSystems() {
        _initializeSystems.clear();
        _executeSystems.clear();
        _cleanupSystems.clear();
        _renderSystems.clear();
        _tearDownSystems.clear();

    }


}
package ilargia.entitas.systems;

import ilargia.entitas.api.system.*;
import ilargia.entitas.factories.EntitasCollections;

import java.util.List;

/**
 * Systems provide a convenient way to group systems.
 * You can add IInitializeSystem, IExecuteSystem, ICleanupSystem, ITearDownSystem, ReactiveSystem and other nested Systems instances.
 * All systems will be initialized and executed based on the order you added them.
 * @author Rubentxu
 */
public class Systems implements IInitializeSystem, IExecuteSystem, IRenderSystem, ICleanupSystem, ITearDownSystem {


    protected List<IInitializeSystem> _initializeSystems; // ObjectArrayList
    protected List<IExecuteSystem> _executeSystems;
    protected List<IRenderSystem> _renderSystems;
    protected List<ICleanupSystem> _cleanupSystems;
    protected List<ITearDownSystem> _tearDownSystems;

    /**
     * Creates a new Systems instance.
     */
    public Systems() {
        _initializeSystems = EntitasCollections.createList(ISystem.class);
        _executeSystems = EntitasCollections.createList(ISystem.class);
        _renderSystems = EntitasCollections.createList(ISystem.class);
        _cleanupSystems = EntitasCollections.createList(ISystem.class);
        _tearDownSystems = EntitasCollections.createList(ISystem.class);

    }

    /**
     * Adds the system instance to the systems list.
     * @param system
     * @return Systems
     */
    public Systems add(ISystem system) {
        if (system != null) {
            if (system instanceof IInitializeSystem) _initializeSystems.add((IInitializeSystem) system);
            if (system instanceof IExecuteSystem) _executeSystems.add((IExecuteSystem) system);
            if (system instanceof IRenderSystem) _renderSystems.add((IRenderSystem) system);
            if (system instanceof ICleanupSystem) _cleanupSystems.add((ICleanupSystem) system);
            if (system instanceof ITearDownSystem) _tearDownSystems.add((ITearDownSystem) system);
        }
        return this;
    }

    /**
     * Calls initialize() on all IInitializeSystem and other
     * nested Systems instances in the order you added them.
     */
    public void initialize() {
        for (IInitializeSystem iSystem : _initializeSystems) {
            iSystem.initialize();
        }
    }

    /**
     * Calls execute() on all IExecuteSystem and other
     * nested Systems instances in the order you added them.
     * @param deltaTime
     */
    public void execute(float deltaTime) {
        for (IExecuteSystem eSystem : _executeSystems) {
            eSystem.execute(deltaTime);
        }
    }

    /**
     * Calls render() on all IRenderSystem and other
     * nested Systems instances in the order you added them.
     */
    @Override
    public void render() {
        for (IRenderSystem eSystem : _renderSystems) {
            eSystem.render();
        }
    }

    /**
     * Calls cleanup() on all ICleanupSystem and other
     * nested Systems instances in the order you added them.
     */
    public void cleanup() {
        for (ICleanupSystem clSystem : _cleanupSystems) {
            clSystem.cleanup();
        }
    }

    /**
     * Calls tearDown() on all ITearDownSystem  and other
     * nested Systems instances in the order you added them.
     */
    public void tearDown() {
        for (ITearDownSystem tSystem : _tearDownSystems) {
            tSystem.tearDown();
        }
    }

    /**
     * Activates all ReactiveSystems in the systems list.
     */
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

    /**
     * Deactivates all ReactiveSystems in the systems list.
     * This will also clear all ReactiveSystems.
     * This is useful when you want to soft-restart your application and want to reuse your existing system instances.
     */
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

    /**
     * Clears all ReactiveSystems in the systems list.
     */
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
        _renderSystems.clear();
        _cleanupSystems.clear();
        _tearDownSystems.clear();

    }

    @Override
    public String toString() {
        return "Systems{" +
                "_initializeSystems=" + _initializeSystems +
                ", _executeSystems=" + _executeSystems +
                ", _renderSystems=" + _renderSystems +
                ", _cleanupSystems=" + _cleanupSystems +
                ", _tearDownSystems=" + _tearDownSystems +
                '}';
    }


}
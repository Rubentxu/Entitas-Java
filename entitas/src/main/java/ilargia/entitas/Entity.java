package ilargia.entitas;


import ilargia.entitas.api.ContextInfo;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.entitas.IAERC;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.events.EntityComponentChanged;
import ilargia.entitas.api.events.EntityComponentReplaced;
import ilargia.entitas.api.events.EntityEvent;
import ilargia.entitas.caching.EntitasCache;
import ilargia.entitas.exceptions.EntityDoesNotHaveComponentException;
import ilargia.entitas.factories.EntitasCollections;
import ilargia.entitas.exceptions.EntityAlreadyHasComponentException;
import ilargia.entitas.exceptions.EntityIsNotEnabledException;

import java.util.List;
import java.util.Set;
import java.util.Stack;

/**
 * Use context.CreateEntity() to create a new entity and entity.Destroy() to destroy it.
 * You can add, replace and remove IComponent to an entity.
 *
 * @author Rubentxu
 **/
public class Entity implements IEntity {

    /**
    * Occurs when a component gets added.
    * All event handlers will be removed when the entity gets destroyed by the context.
    */
    public Set<EntityComponentChanged> OnComponentAdded = EntitasCollections.createSet(EntityComponentChanged.class);

    /**
    * Occurs when a component gets removed.
    * All event handlers will be removed when the entity gets destroyed by the context.
    */
    public Set<EntityComponentChanged> OnComponentRemoved = EntitasCollections.createSet(EntityComponentChanged.class);

    /**
    * Occurs when a component gets replaced.
    * All event handlers will be removed when the entity gets destroyed by the context.
    */
    public Set<EntityComponentReplaced> OnComponentReplaced = EntitasCollections.createSet(EntityComponentReplaced.class);

    /**
     * Occurs when an entity gets released and is not retained anymore.
     * All event handlers will be removed when the entity gets destroyed by the context.
     */
    public Set<EntityEvent> OnEntityReleased = EntitasCollections.createSet(EntityEvent.class);

    /**
     * Occurs when calling entity.Destroy().
     * All event handlers will be removed when the entity gets destroyed by the context.
     */
    public Set<EntityEvent> OnDestroyEntity = EntitasCollections.createSet(EntityEvent.class);


    private int _creationIndex;
    private boolean _isEnabled;
    private int _totalComponents;
    private IComponent[] _components;
    private Stack<IComponent>[] _componentPools;
    private ContextInfo _contextInfo;
    private IAERC aerc;

    private IComponent[] _componentsCache;
    private int[] _componentIndicesCache;


    /**
     *  The total amount of components an entity can possibly have.
     *  @return int
     */
    @Override
    public int getTotalComponents() {
        return _totalComponents;
    }

    /**
    * Each entity has its own unique creationIndex which will be set by the context when you create the entity.
    * @return int
    */
    @Override
    public int getCreationIndex() {
        return _creationIndex;
    }

    /**
    * The context manages the state of an entity.
    * Active entities are enabled, destroyed entities are not.
    * @return boolean
    */
    @Override
    public boolean isEnabled() {
        return _isEnabled;
    }

    /**
     * componentPools is set by the context which created the entity and is used to reuse removed components.
     * Removed components will be pushed to the componentPool.
     * Use entity.CreateComponent(index, type) to get a new or reusable component from the componentPool.
     * Use entity.GetComponentPool(index) to get a componentPool for a specific component index.
     * @return Stack<IComponent>[]
     */
    @Override
    public Stack<IComponent>[] componentPools() {
        return _componentPools;
    }

    /**
     * The contextInfo is set by the context which created the entity and
     * contains information about the context.
     * It's used to provide better error messages.
     * @return ContextInfo
     */
    @Override
    public ContextInfo contextInfo() {
        return _contextInfo;
    }

    /**
     * Automatic Entity Reference Counting (AERC) is used internally to prevent pooling retained entities.
     * If you use retain manually you also have to release it manually at some point.
     * @return IAERC
     */
    @Override
    public IAERC getAERC() {
        return aerc;
    }


    /**
     * Initialize Entity
     *
     * @param creationIndex
     * @param totalComponents
     * @param componentPools
     * @param contextInfo
     * @param aerc
     */
    @Override
    public void initialize(int creationIndex, int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo, IAERC aerc) {
        reactivate(creationIndex);

        _totalComponents = totalComponents;
        _components = new IComponent[totalComponents];
        _componentPools = componentPools;

        if (contextInfo != null) {
            _contextInfo = contextInfo;
        } else {
            _contextInfo = createDefaultContextInfo();
        }
        this.aerc = (aerc == null) ? new SafeAERC(this) : aerc;

    }

    private ContextInfo createDefaultContextInfo() {
        String[] componentNames = new String[_totalComponents];
        for (int i = 0; i < componentNames.length; i++) {
            componentNames[i] = String.valueOf(i);
        }

        return new ContextInfo("No Context", componentNames, null);
    }

    @Override
    public void reactivate(int creationIndex) {
        _creationIndex = creationIndex;
        _isEnabled = true;
    }

    /**
     * Adds a component at the specified index.
     * You can only have one component at an index.
     * Each component type must have its own constant index.
     * The prefered way is to use the
     * generated methods from the code generator.
     *
     * @param index
     * @param component
     */
    @Override
    public void addComponent(int index, IComponent component) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot add component '" + _contextInfo.componentNames[index] + "' to " + this + "!");
        }

        if (hasComponent(index)) {
            throw new EntityAlreadyHasComponentException(index, "Cannot add component '" + _contextInfo.componentNames[index] +
                    "' to " + this + "!", "You should check if an entity already has the component " +
                    "before adding it or use entity.ReplaceComponent().");
        }

        _components[index] = component;
        _componentsCache = null;
        _componentIndicesCache = null;
        notifyComponentAdded(index, component);

    }

    /**
     * Removes a component at the specified index.
     * You can only remove a component at an index if it exists.
     * The prefered way is to use the generated methods from the code generator.
     *
     * @param index
     */
    @Override
    public void removeComponent(int index) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot remove component!" +
                    _contextInfo.componentNames[index] + "' from " + this + "!");
        }

        if (!hasComponent(index)) {
            String errorMsg = "Cannot remove component " + _contextInfo.componentNames[index] +
                    "' from " + this + "You should check if an entity has the component before removing it.";
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        replaceComponentInternal(index, null);

    }

    /**
     * Replaces an existing component at the specified index
     * or adds it if it doesn't exist yet.
     * The prefered way is to use the generated methods from the code generator.
     *
     * @param index
     * @param component
     */
    @Override
    public void replaceComponent(int index, IComponent component) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot replace component!" +
                    _contextInfo.componentNames[index] + "' on " + this + "!");
        }

        if (hasComponent(index)) {
            replaceComponentInternal(index, component);
        } else if (component != null) {
            addComponent(index, component);
        }

    }

    private void replaceComponentInternal(int index, IComponent replacement) {
        IComponent previousComponent = _components[index];

        if (replacement != previousComponent) {
            _components[index] = replacement;
            _componentsCache = null;
            if (replacement != null) {
                notifyComponentReplaced(index, previousComponent, replacement);
            } else {
                _componentIndicesCache = null;
                notifyComponentRemoved(index, previousComponent);
            }
            getComponentPool(index).push(previousComponent);

        } else {
            notifyComponentReplaced(index, previousComponent, replacement);
        }

    }

    /**
     * Returns a component at the specified index.
     * You can only get a component at an index if it exists.
     * The prefered way is to use the generated methods from the code generator.
     *
     * @param index
     * @return IComponent
     */
    @Override
    public IComponent getComponent(int index) {
        if (!hasComponent(index)) {
            String errorMsg = "Cannot get component " +
                    _contextInfo.componentNames[index] + "' from " +
                    this + "!  You should check if an entity has the component before getting it.";
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        return _components[index];

    }

    /**
     * @return IComponent[] Returns all added components.
     */
    @Override
    public IComponent[] getComponents() {
        if (_componentsCache == null) {
            List<IComponent> componentsCache = EntitasCache.getIComponentList();

            for (int i = 0; i < _components.length; i++) {
                IComponent component = _components[i];
                if (component != null) {
                    componentsCache.add(component);
                }
            }
            _componentsCache = new IComponent[componentsCache.size()];
            componentsCache.toArray(_componentsCache);
            EntitasCache.pushIComponentList(componentsCache);
        }
        return _componentsCache;

    }

    /**
     * @return int[] Returns all indices of added components.
     */
    @Override
    public int[] getComponentIndices() {
        if (_componentIndicesCache == null) {
            List<Integer> indices = EntitasCache.getIntArray();
            for (int i = 0; i < _components.length; i++) {
                if (_components[i] != null) {
                    indices.add(i);
                }
            }
            _componentIndicesCache = new int[indices.size()];
            for (int i = 0; i < indices.size(); i++) {
                _componentIndicesCache[i] = indices.get(i);

            }
            EntitasCache.pushIntArray(indices);
        }
        return _componentIndicesCache;

    }

    /**
     * Determines whether this entity has a component at the specified index.
     *
     * @param index
     * @return boolean
     */
    @Override
    public boolean hasComponent(int index) {
        if (index < _components.length)
            return _components[index] != null;
        else
            return false;

    }

    /**
     * Determines whether this entity has components at all the specified indices.
     *
     * @param indices
     * @return boolean
     */
    @Override
    public boolean hasComponents(int... indices) {
        for (int index : indices) {
            if (_components[index] == null) {
                return false;
            }
        }
        return true;

    }

    /**
     * Determines whether this entity has a component at any of the specified indices.
     *
     * @param indices
     * @return boolean
     */
    @Override
    public boolean hasAnyComponent(int... indices) {
        for (int i = 0; i < indices.length; i++) {
            if (_components[indices[i]] != null) {
                return true;
            }
        }
        return false;

    }

    /**
     * Removes all components.
     */
    @Override
    public void removeAllComponents() {
        for (int i = 0; i < _components.length; i++) {
            if (_components[i] != null) {
                replaceComponent(i, null);
            }
        }
    }

    /**
     * Returns the componentPool for the specified component index.
     * componentPools is set by the context which created the entity and is used to reuse removed components.
     * Removed components will be pushed to the componentPool.
     * Use entity.CreateComponent(index, type) to get a new or reusable component from the componentPool.
     *
     * @param index
     * @return Stack<IComponent>
     */
    @Override
    public Stack<IComponent> getComponentPool(int index) {
        Stack<IComponent> componentPool = _componentPools[index];
        if (componentPool == null) {
            componentPool = new Stack<>();
            _componentPools[index] = componentPool;
        }

        return componentPool;
    }

    /**
     * Returns a new or reusable component from the componentPool for the specified component index.
     *
     * @param index
     * @param clazz
     * @return IComponent
     */
    @Override
    public IComponent createComponent(int index, Class clazz) {
        Stack<IComponent> componentContext = getComponentPool(index);
        try {
            if (componentContext.size() > 0) {
                return componentContext.pop();
            } else {
                return (IComponent) clazz.cast(clazz.getConstructor((Class[]) null).newInstance());
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a new or reusable component from the componentPool for the specified component index.
     *
     * @param index
     * @return IComponent
     */
    @Override
    public IComponent createComponent(int index) {
        Stack<IComponent> componentPool = getComponentPool(index);
        try {
            if (componentPool.size() > 0) {
                return componentPool.pop();
            } else {
                Class<?> clazz = _contextInfo.componentTypes[index];
                return (IComponent) clazz.cast(clazz.getConstructor((Class[]) null).newInstance());
            }
        } catch (Exception e) {
            return null;
        }
    }

    public IComponent recoverComponent(int index) {
        Stack<IComponent> componentContext = getComponentPool(index);
        if (componentContext.size() > 0) {
            return componentContext.pop();
        }
        return null;
    }

    /**
     * @return Returns the number of objects that retain this entity.
     */
    @Override
    public int retainCount() {
        return aerc.retainCount();
    }

    /**
     * Retains the entity. An owner can only retain the same entity once.
     * Retain/Release is part of AERC (Automatic Entity Reference Counting) and is used internally to prevent pooling retained entities.
     * If you use retain manually you also have to release it manually at some point.
     *
     * @param owner
     */
    @Override
    public void retain(Object owner) {
        aerc.retain(owner);

    }

    /**
     * Releases the entity. An owner can only release an entity if it retains it.
     * Retain/Release is part of AERC (Automatic Entity Reference Counting) and is used internally to prevent pooling retained entities.
     * If you use retain manually you also have to release it manually at some point.
     *
     * @param owner
     */
    @Override
    public void release(Object owner) {
        aerc.release(owner);
        if (aerc.retainCount() == 0) {
            notifyEntityReleased();
        }

    }

    /**
     * Dispatches OnDestroyEntity which will start the destroy process.
     */
    @Override
    public void destroy() {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot destroy " + this + "!");
        }
        notifyDestroyEntity();

    }

    /**
     * This method is used internally. Don't call it yourself.
     * Use entity.Destroy();
     */
    @Override
    public void internalDestroy() {
        removeAllComponents();
        clearEventsListener();
        _isEnabled = false;
    }

    public void clearEventsListener() {
        if (OnComponentAdded != null) OnComponentAdded.clear();
        if (OnComponentRemoved != null) OnComponentRemoved.clear();
        if (OnComponentReplaced != null) OnComponentReplaced.clear();
        if (OnDestroyEntity != null) OnDestroyEntity.clear();

    }

    /**
     * Do not call this method manually. This method is called by the context.
     */
    public void removeAllOnEntityReleasedHandlers() {
        OnEntityReleased.clear();
    }

    public void OnComponentAdded(EntityComponentChanged listener) {
        if (OnComponentAdded != null) {
            OnComponentAdded = EntitasCollections.createSet(EntityComponentChanged.class);
        }
        OnComponentAdded.add(listener);
    }

    public void OnComponentRemoved(EntityComponentChanged listener) {
        if (OnComponentRemoved != null) {
            OnComponentRemoved = EntitasCollections.createSet(EntityComponentChanged.class);
        }
        OnComponentRemoved.add(listener);
    }

    public void OnComponentReplaced(EntityComponentReplaced listener) {
        if (OnComponentReplaced != null) {
            OnComponentReplaced = EntitasCollections.createSet(EntityComponentReplaced.class);
        }
        OnComponentReplaced.add(listener);
    }

    public void OnEntityReleased(EntityEvent listener) {
        if (OnEntityReleased != null) {
            OnEntityReleased = EntitasCollections.createSet(EntityEvent.class);
        }
        OnEntityReleased.add(listener);
    }

    public void OnDestroyEntity(EntityEvent listener) {
        if (OnDestroyEntity != null) {
            OnDestroyEntity = EntitasCollections.createSet(EntityEvent.class);
        }
        OnDestroyEntity.add(listener);
    }

    public void notifyComponentAdded(int index, IComponent component) {
        if (OnComponentAdded != null) {
            for (EntityComponentChanged listener : OnComponentAdded) {
                listener.changed(this, index, component);
            }
        }
    }

    public void notifyComponentRemoved(int index, IComponent component) {
        if (OnComponentRemoved != null) {
            for (EntityComponentChanged listener : OnComponentRemoved) {
                listener.changed(this, index, component);
            }
        }
    }

    public void notifyComponentReplaced(int index, IComponent previousComponent, IComponent newComponent) {
        if (OnComponentReplaced != null) {
            for (EntityComponentReplaced listener : OnComponentReplaced) {
                listener.replaced(this, index, previousComponent, newComponent);
            }
        }
    }

    public void notifyEntityReleased() {
        if (OnEntityReleased != null) {
            for (EntityEvent listener : OnEntityReleased) {
                listener.released(this);
            }
        }
    }

    public void notifyDestroyEntity() {
        if (OnDestroyEntity != null) {
            for (EntityEvent listener : OnDestroyEntity) {
                listener.released(this);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        return this == (Entity) o;

    }

    @Override
    public int hashCode() {
        return _creationIndex;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "creationIndex=" + _creationIndex +
                ", isEnabled=" + _isEnabled +
                ", contextName=" + _contextInfo.contextName +
                ", components=" + _components +
                ", componentIndicesCache=" + _componentIndicesCache +
                '}';
    }


}
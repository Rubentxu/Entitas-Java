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

public class Entity implements IEntity {

    // Eventos
    public Set<EntityComponentChanged> OnComponentAdded = EntitasCollections.createSet(EntityComponentChanged.class);
    public Set<EntityComponentChanged> OnComponentRemoved = EntitasCollections.createSet(EntityComponentChanged.class);
    public Set<EntityComponentReplaced> OnComponentReplaced = EntitasCollections.createSet(EntityComponentReplaced.class);
    public Set<EntityEvent> OnEntityReleased = EntitasCollections.createSet(EntityEvent.class);
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


    @Override
    public IAERC getAERC() {
        return aerc;
    }

    @Override
    public int getTotalComponents() {
        return _totalComponents;
    }

    @Override
    public int getCreationIndex() {
        return _creationIndex;
    }

    @Override
    public boolean isEnabled() {
        return _isEnabled;
    }

    @Override
    public Stack<IComponent>[] componentPools() {
        return _componentPools;
    }

    @Override
    public ContextInfo contextInfo() {
        return _contextInfo;
    }

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
        this.aerc = (aerc==null)? new SafeAERC(this): aerc;

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

    @Override
    public boolean hasComponent(int index) {
        if (index < _components.length)
            return _components[index] != null;
        else
            return false;

    }

    @Override
    public boolean hasComponents(int... indices) {
        for (int index : indices) {
            if (_components[index] == null) {
                return false;
            }
        }
        return true;

    }

    @Override
    public boolean hasAnyComponent(int... indices) {
        for (int i = 0; i < indices.length; i++) {
            if (_components[indices[i]] != null) {
                return true;
            }
        }
        return false;

    }

    @Override
    public void removeAllComponents() {
        for (int i = 0; i < _components.length; i++) {
            if (_components[i] != null) {
                replaceComponent(i, null);
            }
        }
    }

    @Override
    public Stack<IComponent> getComponentPool(int index) {
        Stack<IComponent> componentPool = _componentPools[index];
        if (componentPool == null) {
            componentPool = new Stack<>();
            _componentPools[index] = componentPool;
        }

        return componentPool;
    }

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

    @Override
    public int retainCount() {
        return aerc.retainCount();
    }

    @Override
    public void retain(Object owner) {
        aerc.retain(owner);

    }

    @Override
    public void release(Object owner) {
        aerc.release(owner);
        if (aerc.retainCount() == 0) {
            notifyEntityReleased();
        }

    }

    @Override
    public void destroy() {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot destroy " + this + "!");
        }
        notifyDestroyEntity();

    }

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
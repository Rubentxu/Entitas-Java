package com.ilargia.games.entitas;


import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.events.EntityComponentChanged;
import com.ilargia.games.entitas.api.events.EntityComponentReplaced;
import com.ilargia.games.entitas.api.events.EntityReleased;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;

import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Entity implements IEntity {

    // Eventos
    public Set<EntityComponentChanged> OnComponentAdded = Collections.createSet(EntityComponentChanged.class);
    public Set<EntityComponentChanged> OnComponentRemoved = Collections.createSet(EntityComponentChanged.class);
    public Set<EntityComponentReplaced> OnComponentReplaced = Collections.createSet(EntityComponentReplaced.class);
    public Set<EntityReleased> OnEntityReleased = Collections.createSet(EntityReleased.class);

    private Set<Object> owners; //ObjectOpenHashSet
    private int _creationIndex;
    private boolean _isEnabled;
    private IComponent[] _components;
    private Stack<IComponent>[] _componentContexts;
    private IComponent[] _componentsCache;
    private int[] _componentIndicesCache;
    private int _totalComponents;
    private ContextInfo _contextInfo;

    public Entity(int totalComponents, Stack<IComponent>[] componentContexts, ContextInfo contextInfo) {
        _components = new IComponent[totalComponents];
        _totalComponents = totalComponents;
        _componentContexts = componentContexts;
        _isEnabled = true;
        owners = Collections.createSet(Object.class);

        if (contextInfo != null) {
            _contextInfo = contextInfo;
        } else {

            String[] componentNames = new String[totalComponents];
            for (int i = 0; i < componentNames.length; i++) {
                componentNames[i] = String.valueOf(i);
            }
            _contextInfo = new ContextInfo("No Context", componentNames, null);
        }
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
        return new Stack[0];
    }

    @Override
    public ContextInfo contextInfo() {
        return _contextInfo;
    }

    @Override
    public void initialize(int creationIndex, int totalComponents, Stack<IComponent>[] componentContexts, ContextInfo contextInfo) {
        reactivate(creationIndex);

        _totalComponents = totalComponents;
        _components = new IComponent[totalComponents];
        _componentContexts = componentContexts;

        if (contextInfo != null) {
            _contextInfo = contextInfo;
        } else {
            _contextInfo = createDefaultContextInfo();
        }

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
                    "' from " + this + "!";
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
            String errorMsg = "Cannot get component at index " +
                    _contextInfo.componentNames[index] + "' from " +
                    this + "!";
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
        try {
            return _components[index] != null;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new EntityDoesNotHaveComponentException("ArrayIndexOutOfBoundsException", index);
        }
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
        Stack<IComponent> componentContext = _componentContexts[index];
        if (componentContext == null) {
            componentContext = new Stack<>();
            _componentContexts[index] = componentContext;
        }

        return componentContext;
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
        Stack<IComponent> componentContext = getComponentPool(index);
        try {
            if (componentContext.size() > 0) {
                return componentContext.pop();
            } else {
                Class<?> clazz = _contextInfo.componentTypes[index];
                return (IComponent) clazz.cast(clazz.getConstructor((Class[]) null).newInstance());
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Set<Object> owners() {
        return owners;
    }

    @Override
    public int retainCount() {
        return owners.size();
    }

    @Override
    public void retain(Object owner) {
        if (!owners.add(owner)) {
            throw new EntityIsAlreadyRetainedByOwnerException(owner);
        }


    }

    @Override
    public void release(Object owner) {
        if (!owners.remove(owner)) {
            throw new EntityIsNotRetainedByOwnerException(owner);
        }

        if (owners.size() == 0) {
            notifyEntityReleased();
        }

    }

    @Override
    public void destroy() {
        removeAllComponents();
        _isEnabled = false;
    }

    ContextInfo createDefaultContextInfo() {
        String[] componentNames = new String[_totalComponents];
        for (int i = 0; i < componentNames.length; i++) {
            componentNames[i] = String.valueOf(i);
        }

        return new ContextInfo("No Context", componentNames, null);
    }

    public IComponent recoverComponent(int index) {
        Stack<IComponent> componentContext = getComponentPool(index);
        if (componentContext.size() > 0) {
            return componentContext.pop();
        }
        return null;
    }

    public void clearEventsListener() {
        if (OnComponentAdded != null) OnComponentAdded.clear();
        if (OnComponentRemoved != null) OnComponentRemoved.clear();
        if (OnComponentReplaced != null) OnComponentReplaced.clear();
        if (OnEntityReleased != null) OnEntityReleased.clear();

    }

    public void removeAllOnEntityReleasedHandlers() {
        OnEntityReleased.clear();
    }

    public void OnComponentAdded(EntityComponentChanged listener) {
        if (OnComponentAdded != null) {
            OnComponentAdded = Collections.createSet(EntityComponentChanged.class);
        }
        OnComponentAdded.add(listener);
    }

    public void OnComponentRemoved(EntityComponentChanged listener) {
        if (OnComponentRemoved != null) {
            OnComponentRemoved = Collections.createSet(EntityComponentChanged.class);
        }
        OnComponentRemoved.add(listener);
    }

    public void OnComponentReplaced(EntityComponentReplaced listener) {
        if (OnComponentReplaced != null) {
            OnComponentReplaced = Collections.createSet(EntityComponentReplaced.class);
        }
        OnComponentReplaced.add(listener);
    }

    public void OnEntityReleased(EntityReleased listener) {
        if (OnEntityReleased != null) {
            OnEntityReleased = Collections.createSet(EntityReleased.class);
        }
        OnEntityReleased.add(listener);
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
            for (EntityReleased listener : OnEntityReleased) {
                listener.released(this);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (_creationIndex != entity._creationIndex) return false;
        if (_totalComponents != entity._totalComponents) return false;
        return _contextInfo != null ? _contextInfo.equals(entity._contextInfo) : entity._contextInfo == null;
    }

    @Override
    public int hashCode() {
        return _creationIndex;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "_creationIndex=" + _creationIndex +
                ", _isEnabled=" + _isEnabled +
                ", _contextInfo=" + _contextInfo +
                '}';
    }


}
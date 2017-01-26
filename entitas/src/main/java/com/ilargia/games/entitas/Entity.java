package com.ilargia.games.entitas;


import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.events.EntityComponentChanged;
import com.ilargia.games.entitas.api.events.EntityComponentReplaced;
import com.ilargia.games.entitas.api.events.EntityReleased;
import com.ilargia.games.entitas.api.events.Event;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;

import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Entity implements IEntity {

    // Eventos
    public Event<EntityComponentChanged> OnComponentAdded;
    public Event<EntityComponentChanged> OnComponentRemoved;
    public Event<EntityComponentReplaced> OnComponentReplaced;
    public Event<EntityReleased> OnEntityReleased;

    private Set<Object> owners; //ObjectOpenHashSet
    private int _creationIndex;
    private boolean _isEnabled;
    private IComponent[] _components;
    private Stack<IComponent>[] _componentPools;
    private IComponent[] _componentsCache;
    private int[] _componentIndicesCache;
    private String _toStringCache;
    private int _totalComponents;
    private ContextInfo _contextInfo;
    private StringBuilder _toStringBuilder;

    public Entity(int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo) {
        _components = new IComponent[totalComponents];
        _totalComponents = totalComponents;
        _componentPools = componentPools;
        _isEnabled = true;
        owners = Collections.createSet(Object.class);

        OnComponentAdded = new Event<>();
        OnComponentRemoved = new Event<>();
        OnComponentReplaced = new Event<>();
        OnEntityReleased = new Event<>();


        if (contextInfo != null) {
            _contextInfo = contextInfo;
        } else {

            String[] componentNames = new String[totalComponents];
            for (int i = 0; i < componentNames.length; i++) {
                componentNames[i] = String.valueOf(i);
            }
            _contextInfo = new ContextInfo("No Pool", componentNames, null);
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
    public void initialize(int creationIndex, int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo) {
        reactivate(creationIndex);

        _totalComponents = totalComponents;
        _components = new IComponent[totalComponents];
        _componentPools = componentPools;

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
        _toStringCache = null;
        notifyComponentAdded(this, index, component);

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
        _toStringCache = null;
        IComponent previousComponent = _components[index];

        if (replacement != previousComponent) {
            _components[index] = replacement;
            _componentsCache = null;
            if (replacement != null) {
                notifyComponentReplaced(this, index, previousComponent, replacement);
            } else {
                _componentIndicesCache = null;
                notifyComponentRemoved(this, index, previousComponent);
            }
            getComponentPool(index).push(previousComponent);

        } else {
            notifyComponentReplaced(this, index, previousComponent, replacement);
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
        _toStringCache = null;
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
        Stack<IComponent> componentPool = getComponentPool(index);
        try {
            if (componentPool.size() > 0) {
                return componentPool.pop();
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
        _toStringCache = null;

    }

    @Override
    public void release(Object owner) {
        if (!owners.remove(owner)) {
            throw new EntityIsNotRetainedByOwnerException(owner);
        }

        if (owners.size() == 0) {
            _toStringCache = null;
            notifyEntityReleased(this);
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
        Stack<IComponent> componentPool = getComponentPool(index);
        if (componentPool.size() > 0) {
            return componentPool.pop();
        }
        return null;
    }

    @Override
    public void removeAllOnEntityReleasedHandlers() {
        // OnEntityReleased = null;
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            if (_toStringBuilder == null) {
                _toStringBuilder = new StringBuilder();
            }

            _toStringBuilder
                    .append("Entity_")
                    .append(_creationIndex)
                    .append("(*")
                    .append(retainCount())
                    .append(")")
                    .append("(");

            String separator = ", ";
            IComponent[] components = getComponents();
            int lastSeparator = components.length - 1;
            for (int i = 0; i < components.length; i++) {
                IComponent component = components[i];
                Object type = component.getClass();
//                implementsToString = type.getMethod("ToString")
//                        .DeclaringType == type;
//                _toStringBuilder.append(
//                        implementsToString
//                                ? component.ToString()
//                                : type.Name.RemoveComponentSuffix()
//                );

                if (i < lastSeparator) {
                    _toStringBuilder.append(separator);
                }
            }

            _toStringBuilder.append(")");
            _toStringCache = _toStringBuilder.toString();
        }

        return _toStringCache;
    }

    @Override
    public int hashCode() {
        return _creationIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Entity)) return false;

        IEntity other = (IEntity) o;
        if (this._creationIndex != other.getCreationIndex()) return false;
        return true;
    }

    public void notifyComponentAdded(IEntity entity, int index, IComponent component) {
        for (EntityComponentChanged listener : OnComponentAdded.listeners()) {
            listener.changed(entity, index, component);
        }
    }

    public void notifyComponentRemoved(IEntity entity, int index, IComponent component) {
        for (EntityComponentChanged listener : OnComponentRemoved.listeners()) {
            listener.changed(entity, index, component);
        }
    }

    public void notifyComponentReplaced(IEntity entity, int index, IComponent previousComponent, IComponent newComponent) {
        for (EntityComponentReplaced listener : OnComponentReplaced.listeners()) {
            listener.replaced(entity, index, previousComponent, newComponent);
        }
    }

    public void notifyEntityReleased(IEntity entity) {
        for (EntityReleased listener : OnEntityReleased.listeners()) {
            listener.released(entity);
        }
    }

}
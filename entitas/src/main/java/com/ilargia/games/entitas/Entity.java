package com.ilargia.games.entitas;


import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException;
import com.ilargia.games.entitas.exceptions.EntityDoesNotHaveComponentException;
import com.ilargia.games.entitas.exceptions.EntityIsNotEnabledException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.interfaces.IComponent;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Entity {

    public Set<Object> owners; //ObjectOpenHashSet
    public int _retainCount;
    private int _creationIndex;
    private boolean _isEnabled;
    private IComponent[] _components;
    private Stack<IComponent>[] _componentPools;
    private IComponent[] _componentsCache;
    private int[] _componentIndicesCache;
    private String _toStringCache;
    private int _totalComponents;
    private ContextInfo _contextInfo;
    private EventBus _eventBus;

    public Entity(int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo, EventBus eventBus) {
        _components = new IComponent[totalComponents];
        _totalComponents = totalComponents;
        _componentPools = componentPools;
        _isEnabled = true;
        owners = Collections.createSet(Object.class);
        _eventBus = eventBus;

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

    public Entity addComponent(int index, IComponent component) {
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
        _eventBus.notifyComponentAdded(this, index, component);
        return this;

    }

    public Entity removeComponent(int index) {
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

        return this;
    }

    public Entity replaceComponent(int index, IComponent component) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot replace component!" +
                    _contextInfo.componentNames[index] + "' on " + this + "!");
        }

        if (hasComponent(index)) {
            replaceComponentInternal(index, component);
        } else if (component != null) {
            addComponent(index, component);
        }
        return this;
    }

    private void replaceComponentInternal(int index, IComponent replacement) {
        _toStringCache = null;
        IComponent previousComponent = _components[index];

        if (replacement != previousComponent) {
            _components[index] = replacement;
            _componentsCache = null;
            if (replacement != null) {
                _eventBus.notifyComponentReplaced(this, index, previousComponent, replacement);
            } else {
                _componentIndicesCache = null;
                _eventBus.notifyComponentRemoved(this, index, previousComponent);
            }
            getComponentPool(index).push(previousComponent);

        } else {
            _eventBus.notifyComponentReplaced(this, index, previousComponent, replacement);
        }

    }

    public IComponent getComponent(int index) {
        if (!hasComponent(index)) {
            String errorMsg = "Cannot get component at index " +
                    _contextInfo.componentNames[index] + "' from " +
                    this + "!";
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        return _components[index];

    }

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


    public int[] getComponentIndices() {
        if (_componentIndicesCache == null) {
            List<Integer> indices = EntitasCache.getIntArray();
            for (int i = 0; i < _components.length; i++) {
                if (_components[i] != null) {
                    indices.add(i);
                }
            }
            _componentIndicesCache =  new int[indices.size()];
            for (int i = 0; i < indices.size(); i++) {
                _componentIndicesCache[i] = indices.get(i);

            }
            EntitasCache.pushIntArray(indices);
        }
        return _componentIndicesCache;

    }

    public boolean hasComponent(int index) {
        try {
            return _components[index] != null;
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new EntityDoesNotHaveComponentException("ArrayIndexOutOfBoundsException", index);
        }
    }

    public boolean hasComponents(int... indices) {
        for (int index : indices) {
            if (_components[index] == null) {
                return false;
            }
        }
        return true;

    }

    public boolean hasAnyComponent(int... indices) {
        for (int i = 0; i < indices.length; i++) {
            if (_components[indices[i]] != null) {
                return true;
            }
        }
        return false;

    }

    public void removeAllComponents() {
        _toStringCache = null;
        for (int i = 0; i < _components.length; i++) {
            if (_components[i] != null) {
                replaceComponent(i, null);
            }
        }
    }

    private Stack<IComponent> getComponentPool(int index) {
        Stack<IComponent> componentPool = _componentPools[index];
        if (componentPool == null) {
            componentPool = new Stack<IComponent>();
            _componentPools[index] = componentPool;
        }

        return componentPool;
    }

    public IComponent recoverComponent(int index) {
        Stack<IComponent> componentPool = getComponentPool(index);
        if (componentPool.size() > 0) {
            return componentPool.pop();
        }
        return null;
    }


    public <T extends IComponent> T createComponent(int index) {
        Stack<IComponent> componentPool = getComponentPool(index);
        try {
            if (componentPool.size() > 0) {
                return (T) componentPool.pop();
            } else {
                Class<T> clazz = _contextInfo.componentTypes[index];
                return clazz.cast(clazz.getConstructor((Class[]) null).newInstance());
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void destroy() {
        removeAllComponents();
        _isEnabled = false;
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            StringBuilder sb = (new StringBuilder()).append("Entity_").append(_creationIndex);
//
//            final String SEPARATOR = ", ";
//            IComponent[] components = getComponents();
//            int lastSeparator = components.length - 1;
//            for (int i = 0, componentsLength = components.length; i < componentsLength; i++) {
//                sb.append(components[i].getClass().getName());
//                if (i < lastSeparator) {
//                    sb.append(SEPARATOR);
//                }
//            }

            _toStringCache = sb.toString();
        }
        return _toStringCache;
    }

    public int getRetainCount() {
        return _retainCount;
    }

    public Entity retain(Object owner) {
        _retainCount += 1;
        _toStringCache = null;
        return this;
    }

    public void release(Object owner) {
        _retainCount -= 1;

        if (_retainCount == 0) {
            _toStringCache = null;
            _eventBus.notifyEntityReleased(this);
        }
    }

    public int getCreationIndex() {
        return _creationIndex;
    }

    public void setCreationIndex(int _creationIndex) {
        this._creationIndex = _creationIndex;
    }

    public boolean isEnabled() {
        return _isEnabled;
    }

    public void setEnabled(boolean _isEnabled) {
        this._isEnabled = _isEnabled;
    }


}
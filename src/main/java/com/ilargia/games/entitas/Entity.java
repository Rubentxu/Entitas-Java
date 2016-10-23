package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.ComponentReplaced;
import com.ilargia.games.entitas.interfaces.EntityChanged;
import com.ilargia.games.entitas.interfaces.EntityReleased;

import java.lang.reflect.ParameterizedType;
import java.util.Stack;

public class Entity {

    public EntityChanged OnComponentAdded;
    public EntityChanged OnComponentRemoved;
    public ComponentReplaced OnComponentReplaced;
    public EntityReleased OnEntityReleased;

    private int _creationIndex;
    private boolean _isEnabled;
    private IComponent[] _components;
    private Stack<IComponent>[] _componentPools;
    private IComponent[] _componentsCache;
    private Integer[] _componentIndicesCache;
    private String _toStringCache;
    private int _totalComponents;
    private PoolMetaData _poolMetaData;

    public ObjectSet<Object> owners;



    public Entity(int totalComponents, Stack<IComponent>[] componentPools, PoolMetaData poolMetaData) {
        _components = new IComponent[totalComponents];
        _totalComponents = totalComponents;
        _componentPools = componentPools;
        _isEnabled = true;
        owners = new ObjectSet<>();

        if(poolMetaData != null) {
            _poolMetaData = poolMetaData;
        } else {

            String[] componentNames = new String[totalComponents];
            for(Integer i = 0; i < componentNames.length; i++) {
                componentNames[i] = i.toString();
            }
            _poolMetaData = new PoolMetaData("No Pool", componentNames, null);
        }

    }


    public Entity addComponent(int index, IComponent component) {
        if(!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot add component '" + _poolMetaData.componentNames[index] + "' to " + this + "!");
        }

        if(hasComponent(index)) {
            throw new EntityAlreadyHasComponentException(index, "Cannot add component '" + _poolMetaData.componentNames[index] +
                            "' to " + this + "!", "You should check if an entity already has the component " +
                            "before adding it or use entity.ReplaceComponent().");
        }

        _components[index] = component;
        _componentsCache = null;
        _componentIndicesCache = null;
        _toStringCache = null;
        if(OnComponentAdded != null) {
            OnComponentAdded.entityChanged(this, index, component);
        }
        return this;

    }

    public Entity removeComponent(int index) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot remove component!"+
                    _poolMetaData.componentNames[index] + "' from " + this + "!");
        }

        if (!hasComponent(index)) {
            String errorMsg = "Cannot remove component " + _poolMetaData.componentNames[index] +
                    "' from " + this + "!";
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        replaceComponentInternal(index, null);

        return this;
    }


    public Entity replaceComponent(int index, IComponent component) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot replace component!"+
                    _poolMetaData.componentNames[index] + "' on " + this + "!");
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

        if(replacement != previousComponent) {
            _components[index] = replacement;
            _componentsCache = null;
            if(replacement != null) {
                if(OnComponentReplaced != null) {
                    OnComponentReplaced.componentReplaced(this, index, previousComponent, replacement);
                }
            } else {
                _componentIndicesCache = null;
                if(OnComponentRemoved != null) {
                    OnComponentRemoved.entityChanged(this, index, previousComponent);
                }
            }
            getComponentPool(index).push(previousComponent);

        } else {
            if(OnComponentReplaced != null) {
                OnComponentReplaced.componentReplaced(this, index, previousComponent, replacement);
            }
        }

    }

    public IComponent getComponent(int index) {
        if (!hasComponent(index)) {
            String errorMsg = "Cannot get component at index " +
                    _poolMetaData.componentNames[index] + "' from " +
                    this + "!";
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        return _components[index];

    }

    public IComponent[] getComponents() {
        if (_componentsCache == null) {
            Array<IComponent> components = EntitasCache.getIComponentList();
            for (int i = 0; i < _components.length; i++) {
                IComponent component = _components[i];
                if (component != null) {
                    components.add(component);
                }
            }

            _componentsCache = components.items;
            EntitasCache.pushIComponentList(components);
        }
        return _componentsCache;

    }

    public Integer[] getComponentIndices() {
        if (_componentIndicesCache == null) {
            Array<Integer> indices = EntitasCache.getIntList();
            for (int i = 0; i < _components.length; i++) {
                if (_components[i] != null) {
                    indices.add(i);
                }
            }
            _componentIndicesCache = indices.toArray();
            EntitasCache.pushIntList(indices);
        }
        return _componentIndicesCache;

    }

    public boolean hasComponent(int index) {
        try {
            return _components[index] != null;
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public boolean hasComponents(int[] indices) {
        for (int index : indices) {
            if (_components[index] == null) {
                return false;
            }
        }
        return true;

    }

    public boolean hasAnyComponent(int[] indices) {
        for(int i = 0; i < indices.length; i++) {
            if(_components[indices[i]] != null) {
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

    public Stack<IComponent> getComponentPool(int index) {
        Stack<IComponent> componentPool = _componentPools[index];
        if(componentPool == null) {
            componentPool = new Stack<IComponent>();
            _componentPools[index] = componentPool;
        }

        return componentPool;
    }


    public <T>  T createComponent(int index) throws IllegalAccessException, InstantiationException {
        Class<T> clazz = (Class<T>)
                ((ParameterizedType)getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        Stack<IComponent> componentPool = getComponentPool(index);
        return componentPool.size() > 0 ? (T)componentPool.pop() : clazz.newInstance();
    }


    public void destroy() {
        removeAllComponents();
        OnComponentAdded = null;
        OnComponentReplaced = null;
        OnComponentRemoved = null;
        _isEnabled = false;
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            StringBuilder sb = (new StringBuilder()).append("Entity_").append(_creationIndex).append("(").
                    append(getRetainCount()).append(")").append("(");

            final String SEPARATOR = ", ";
            IComponent[] components = getComponents();
            int lastSeparator = components.length - 1;
            for (int i = 0, componentsLength = components.length; i < componentsLength; i++) {
                sb.append(components[i].getClass().getName());
                if (i < lastSeparator) {
                    sb.append(SEPARATOR);
                }
            }

            sb.append(")");
            _toStringCache = sb.toString();
        }
        return _toStringCache;
    }

    int _retainCount;
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

        if(_retainCount == 0) {
            _toStringCache = null;
            if (OnEntityReleased != null) {
                OnEntityReleased.entityReleased(this);
            }
        }
    }

    void removeAllOnEntityReleasedHandlers() {
        OnEntityReleased = null;
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

    public Stack<IComponent>[] getComponentPools() {
        return _componentPools;
    }

}
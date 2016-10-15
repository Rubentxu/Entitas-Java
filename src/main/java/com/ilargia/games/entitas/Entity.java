package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.events.Event;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.ComponentReplaced;
import com.ilargia.games.entitas.interfaces.EntityChanged;
import com.ilargia.games.entitas.interfaces.EntityReleased;
import com.ilargia.games.entitas.interfaces.IComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Entity {

    private int _creationIndex;
    private boolean _isEnabled;
    private IComponent[] _components;
    private IComponent[] _componentsCache;
    private IntArray _componentIndicesCache;
    private String _toStringCache;

    public final ObjectSet<Object> owners;
    public Event<EntityChanged> OnComponentAdded;
    public Event<EntityChanged> OnComponentRemoved;
    public Event<ComponentReplaced> OnComponentReplaced;
    public Event<EntityReleased> OnEntityReleased;


    public Entity(int totalComponents) {
        _components = new IComponent[totalComponents];
        _isEnabled = true;
        owners = new ObjectSet<>();
        OnComponentAdded = new Event<EntityChanged>();
        OnComponentRemoved = new Event<EntityChanged>();
        OnComponentReplaced = new Event<ComponentReplaced>();
        OnEntityReleased = new Event<EntityReleased>();

    }

    public Entity addComponent(int index, IComponent component) {
        if(index > _components.length-1) {
            String errorMsg = "Cannot add component at index " + index + "; Max index " +_components.length;
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }

        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot add component!");
        }

        if (hasComponent(index)) {
            throw new EntityAlreadyHasComponentException("Cannot add component at index " + index + " to " + this, index);
        }

        _components[index] = component;
        _componentsCache = null;
        _componentIndicesCache = null;
        _toStringCache = null;
        if (OnComponentAdded != null) {
            for (EntityChanged listener : OnComponentAdded.listeners()) {
                listener.entityChanged(this, index, component);
            }
        }
        return this;

    }

    public Entity removeComponent(int index) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot remove component!");
        }

        if (!hasComponent(index)) {
            String errorMsg = "Cannot remove component at index " + index + " from " + this;
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        replaceComponentInternal(index, null);

        return this;
    }

    public Entity replaceComponent(int index, IComponent component) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot replace component!");
        }

        if (hasComponent(index)) {
            replaceComponentInternal(index, component);
        } else if (component != null) {
            addComponent(index, component);
        }
        return this;
    }

    private void replaceComponentInternal(int index, IComponent replacement) {
        IComponent previousComponent = _components[index];
        if (previousComponent == replacement) {
            if (OnComponentReplaced != null) {
                for (ComponentReplaced listener : OnComponentReplaced.listeners()) {
                    listener.componentReplaced(this, index, previousComponent, replacement);
                }
            }
        } else {
            _components[index] = replacement;
            _componentsCache = null;
            if (replacement == null) {
                _componentIndicesCache = null;
                _toStringCache = null;
                if (OnComponentRemoved != null) {
                    for (EntityChanged listener : OnComponentRemoved.listeners()) {
                        listener.entityChanged(this, index, previousComponent);
                    }
                }
            } else {
                if (OnComponentReplaced != null) {
                    for (ComponentReplaced listener : OnComponentReplaced.listeners()) {
                        listener.componentReplaced(this, index, previousComponent, replacement);
                    }
                }
            }
        }
    }

    public <T extends IComponent> T getComponent(int index) {
        if (!hasComponent(index)) {
            String errorMsg = "Cannot get component at index " + index + " from " + this;
            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }
        return (T) _components[index];
    }

    public IComponent[] getComponents() {
        if (_componentsCache == null) {
            List<IComponent> components = new ArrayList<IComponent>();
            for (int i = 0, componentsLength = _components.length; i < componentsLength; i++) {
                IComponent component = _components[i];
                if (component != null) {
                    components.add(component);
                }
            }

            _componentsCache = components.toArray(new IComponent[0]);
        }
        return _componentsCache;

    }

    public IntArray getComponentIndices() {
        if (_componentIndicesCache == null) {
            IntArray indices = new IntArray();
            for (int i = 0, componentsLength = _components.length; i < componentsLength; i++) {
                if (_components[i] != null) {
                    indices.add(i);
                }
            }
            _componentIndicesCache = indices;
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

    public boolean hasComponents(IntArray indices) {
        for (int index : indices.items) {
            if (_components[index] == null) {
                return false;
            }
        }
        return true;

    }

    public boolean hasAnyComponent(IntArray indices) {
        for (int index : indices.items) {
            if (_components[index] != null) {
                return true;
            }
        }
        return false;

    }

    public void removeAllComponents() {
        _toStringCache = null;
        for (int i = 0, componentsLength = _components.length; i < componentsLength; i++) {
            if (_components[i] != null) {
                replaceComponent(i, null);
            }
        }
    }

    public void destroy() {
        removeAllComponents();
        OnComponentAdded.clear();
        OnComponentReplaced.clear();
        OnComponentRemoved.clear();
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

    public int getRetainCount() {
        return owners.size;
    }

    public Entity retain(Object owner) {
        if (!owners.add(owner)) {
            throw new EntityIsAlreadyRetainedByOwnerException(owner);
        }
        return this;
    }

    public void release(Object owner) {
        if (!owners.remove(owner)) {
            throw new EntityIsNotRetainedByOwnerException(owner);
        }

        if (owners.size < 1) {
            if (OnEntityReleased != null) {
                for (EntityReleased listener : OnEntityReleased.listeners()) {
                    listener.entityReleased(this);
                }
            }
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
package com.ilargia.games.entitas;

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

    public final HashSet<Object> owners = new HashSet<>();
    public Event<EntityChanged> OnComponentAdded = new Event<EntityChanged>();
    public Event<EntityChanged> OnComponentRemoved = new Event<EntityChanged>();
    public Event<ComponentReplaced> OnComponentReplaced = new Event<ComponentReplaced>();
    public String[] componentNames;
    public int _creationIndex;
    public boolean _isEnabled = true;
    public Event<EntityReleased> OnEntityReleased = new Event<EntityReleased>();
    private IComponent[] _components;
    private IComponent[] _componentsCache;
    private List<Integer> _componentIndicesCache;
    private String _toStringCache;


    public Entity(int totalComponents) {
        _components = new IComponent[totalComponents];
    }

    public int getcreationIndex() {
        return _creationIndex;
    }

    public Entity addComponent(int index, IComponent component) {
        if (!_isEnabled) {
            throw new EntityIsNotEnabledException("Cannot add component!");
        }

        if (hasComponent(index)) {
            String errorMsg;
            if (componentNames == null) {
                errorMsg = "Cannot add component at index " + index + " to " + this;
            } else {
                errorMsg = "Cannot add component " + componentNames[index] + " to " + this;
            }

            throw new EntityAlreadyHasComponentException(errorMsg, index);
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
            String errorMsg;
            if (componentNames == null) {
                errorMsg = "Cannot remove component at index " + index + " from " + this;
            } else {
                errorMsg = "Cannot remove component " + componentNames[index] + " from " + this;
            }

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

    public IComponent getComponent(int index) {
        if (!hasComponent(index)) {
            String errorMsg;
            if (componentNames == null) {
                errorMsg = "Cannot get component at index " + index + " from " + this;
            } else {
                errorMsg = "Cannot get component " + componentNames[index] + " from " + this;
            }

            throw new EntityDoesNotHaveComponentException(errorMsg, index);
        }

        return _components[index];
    }

    public IComponent[] getComponents() {
        if (_componentsCache == null) {
            ArrayList<IComponent> components = new ArrayList<IComponent>();
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

    public List<Integer> getComponentIndices() {
        if (_componentIndicesCache == null) {
            ArrayList<Integer> indices = new ArrayList<Integer>();
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
        return _components[index] != null;
    }

    public boolean hasComponents(List<Integer> indices) {
        for (int i = 0, indicesLength = indices.size(); i < indicesLength; i++) {
            if (_components[indices.get(i)] == null) {
                return false;
            }
        }

        return true;
    }

    public boolean hasAnyComponent(List<Integer> indices) {
        for (int i = 0, indicesLength = indices.size(); i < indicesLength; i++) {
            if (_components[indices.get(i)] != null) {
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
        OnComponentAdded = null;
        OnComponentReplaced = null;
        OnComponentRemoved = null;
        componentNames = null;
        _isEnabled = false;
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            StringBuilder sb = (new StringBuilder()).append("Entity_").append(_creationIndex).append("(").append(getretainCount()).append(")").append("(");

            final String sEPARATOR = ", ";
            IComponent[] components = getComponents();
            int lastSeparator = components.length - 1;
            for (int i = 0, componentsLength = components.length; i < componentsLength; i++) {
                sb.append(components[i].getClass().getName());
                if (i < lastSeparator) {
                    sb.append(sEPARATOR);
                }
            }

            sb.append(")");
            _toStringCache = sb.toString();
        }
        return _toStringCache;
    }

    public int getretainCount() {
        return owners.size();
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

        if (owners.isEmpty()) {
            if (OnEntityReleased != null) {
                for (EntityReleased listener : OnEntityReleased.listeners()) {
                    listener.entityReleased(this);
                }
            }
        }
    }


}
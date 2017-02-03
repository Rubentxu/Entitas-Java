package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.events.*;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

public interface IEntity {

    // Eventos
    Map<IEntity,Set<EntityComponentChanged>> OnComponentAdded =  Collections.createMap(IEntity.class, Set.class);
    Map<IEntity,Set<EntityComponentChanged>> OnComponentRemoved =  Collections.createMap(IEntity.class, Set.class);
    Map<IEntity,Set<EntityComponentReplaced>> OnComponentReplaced =  Collections.createMap(IEntity.class, Set.class);
    Map<IEntity,Set<EntityReleased>> OnEntityReleased =  Collections.createMap(IEntity.class, Set.class);

    int getTotalComponents();

    int getCreationIndex();

    boolean isEnabled();

    Stack<IComponent>[] componentPools();

    ContextInfo contextInfo();

    void initialize(int creationIndex, int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo);

    void reactivate(int creationIndex);

    void addComponent(int index, IComponent component);

    void removeComponent(int index);

    void replaceComponent(int index, IComponent component);

    IComponent getComponent(int index);

    IComponent[] getComponents();

    int[] getComponentIndices();

    boolean hasComponent(int index);

    boolean hasComponents(int[] indices);

    boolean hasAnyComponent(int[] indices);

    void removeAllComponents();

    Stack<IComponent> getComponentPool(int index);

    IComponent createComponent(int index, Class type);

    <T> T createComponent(int index);

    Set<Object> owners();

    int retainCount();

    void retain(Object owner);

    void release(Object owner);

    void destroy();

    default void clearEventsListener() {
        if(OnComponentAdded.containsKey(this)) OnComponentAdded.get(this).clear();
        if(OnComponentRemoved.containsKey(this)) OnComponentRemoved.get(this).clear();
        if(OnComponentReplaced.containsKey(this)) OnComponentReplaced.get(this).clear();
        if(OnEntityReleased.containsKey(this)) OnEntityReleased.get(this).clear();

    }

    default void removeAllOnEntityReleasedHandlers(){
        OnEntityReleased.get(this).clear();
    }

    default void OnComponentAdded(EntityComponentChanged listener ) {
        if (!OnComponentAdded.containsKey(this)) {
            OnComponentAdded.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnComponentAdded.get(this).add(listener);
    }

    default void OnComponentRemoved(EntityComponentChanged listener ) {
        if (!OnComponentRemoved.containsKey(this)) {
            OnComponentRemoved.put(this, Collections.createSet(EntityComponentChanged.class));
        }
        OnComponentRemoved.get(this).add(listener);
    }

    default void OnComponentReplaced(EntityComponentReplaced listener ) {
        if (!OnComponentReplaced.containsKey(this)) {
            OnComponentReplaced.put(this, Collections.createSet(EntityComponentReplaced.class));
        }
        OnComponentReplaced.get(this).add(listener);
    }

    default void OnEntityReleased(EntityReleased listener ) {
        if (!OnEntityReleased.containsKey(this)) {
            OnEntityReleased.put(this, Collections.createSet(EntityReleased.class));
        }
        OnEntityReleased.get(this).add(listener);
    }

    default void notifyComponentAdded(int index, IComponent component) {
        if (OnComponentAdded.containsKey(this)) {
            for (EntityComponentChanged listener : OnComponentAdded.get(this)) {
                listener.changed(this, index, component);
            }
        }
    }

    default void notifyComponentRemoved(int index, IComponent component) {
        if (OnComponentRemoved.containsKey(this)) {
            for (EntityComponentChanged listener : OnComponentRemoved.get(this)) {
                listener.changed(this, index, component);
            }
        }
    }

    default void notifyComponentReplaced(int index, IComponent previousComponent, IComponent newComponent) {
        if (OnComponentReplaced.containsKey(this)) {
            for (EntityComponentReplaced listener : OnComponentReplaced.get(this)) {
                listener.replaced(this, index, previousComponent, newComponent);
            }
        }
    }

    default void notifyEntityReleased() {
        if (OnEntityReleased.containsKey(this)) {
            for (EntityReleased listener : OnEntityReleased.get(this)) {
                listener.released(this);
            }
        }
    }


}

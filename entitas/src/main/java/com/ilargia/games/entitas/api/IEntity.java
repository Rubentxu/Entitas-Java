package com.ilargia.games.entitas.api;

import com.ilargia.games.entitas.api.events.*;

import java.util.Set;
import java.util.Stack;

public interface IEntity {

    // Eventos
    Event<EntityComponentChanged> OnComponentAdded = new Event<>();
    Event<EntityComponentChanged> OnComponentRemoved = new Event<>();
    Event<EntityComponentReplaced> OnComponentReplaced = new Event<>();
    Event<EntityReleased> OnEntityReleased = new Event<>();


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

    void removeAllOnEntityReleasedHandlers();

    default void notifyComponentAdded(IEntity entity, int index, IComponent component) {
        for (EntityComponentChanged listener : OnComponentAdded.listeners()) {
            listener.changed(entity, index, component);
        }
    }

    default void notifyComponentRemoved(IEntity entity, int index, IComponent component) {
        for (EntityComponentChanged listener : OnComponentRemoved.listeners()) {
            listener.changed(entity, index, component);
        }
    }

    default void notifyComponentReplaced(IEntity entity, int index, IComponent previousComponent, IComponent newComponent) {
        for (EntityComponentReplaced listener : OnComponentReplaced.listeners()) {
            listener.replaced(entity, index, previousComponent, newComponent);
        }
    }

    default void notifyEntityReleased(IEntity entity) {
        for (EntityReleased listener : OnEntityReleased.listeners()) {
            listener.released(entity);
        }
    }
}

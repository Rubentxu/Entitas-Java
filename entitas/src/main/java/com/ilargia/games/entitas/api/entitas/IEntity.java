package com.ilargia.games.entitas.api.entitas;

import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.IComponent;

import java.util.Set;
import java.util.Stack;

public interface IEntity extends IAERC {

    IAERC getAERC();

    int getTotalComponents();

    int getCreationIndex();

    boolean isEnabled();

    Stack<IComponent>[] componentPools();

    ContextInfo contextInfo();

    void initialize(int creationIndex, int totalComponents, Stack<IComponent>[] componentPools, ContextInfo contextInfo, IAERC aerc);

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

    int retainCount();

    void retain(Object owner);

    void release(Object owner);

    void destroy();

    void internalDestroy();


}

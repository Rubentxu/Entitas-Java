package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.components.Interactive;
import com.ilargia.games.entitas.components.Motion;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.View;
import com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException;
import com.ilargia.games.entitas.exceptions.EntityDoesNotHaveComponentException;
import com.ilargia.games.entitas.exceptions.EntityIsNotEnabledException;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static org.junit.Assert.*;

public class EntityTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private TestEntity entity;
    private Stack<IComponent>[] _componentPools;


    private void createCollections() {
        new EntitasCollections(new CollectionsFactories() {

        });
    }

    @Before
    public void setUp() throws Exception {
        createCollections();
        _componentPools = new Stack[10];
        EntitasCache cache = new EntitasCache();
        entity = new TestEntity();
        entity.initialize(0, 10, _componentPools, new ContextInfo("Test", TestComponentIds.componentNames(),
                TestComponentIds.componentTypes()));
        entity.clearEventsListener();
        entity.reactivate(0);
        entity.addComponent(TestComponentIds.Position, new Position(100, 100));
        entity.addComponent(TestComponentIds.View, new View(1));


    }


    //@Test
    public void componentTypes() {
       /* assertEquals(0,  IComponent.getIdComponent(Position.class));
        assertEquals(1,  IComponent.getIdComponent(Movable.class));
        assertEquals(2, IComponent.getIdComponent(Views.class));
*/
    }

    @Test
    public void EntityHashComponent() {
        assertEquals(true, entity.hasComponent(TestComponentIds.Position));

    }

    @Test
    public void EntityDoesNotHaveComponent() {
        assertEquals(false, entity.hasComponent(TestComponentIds.Score));

    }

    @Test
    public void EntityHasSomeComponent() {
        assertEquals(true, entity.hasAnyComponent(TestComponentIds.Position, TestComponentIds.Player));
    }

    @Test
    public void EntityDoesNotHaveAnyComponent() {
        assertEquals(false, entity.hasAnyComponent(TestComponentIds.Score));

    }

    @Test(expected = EntityAlreadyHasComponentException.class)
    public void EntityAlreadyHasComponentException() {
        entity.addComponent(TestComponentIds.Position, new Position(100, 100));

    }

    @Test
    public void OnComponentAddedTest() {
        entity.OnComponentAdded((IEntity e, int index, IComponent c) -> assertEquals(TestComponentIds.Motion, index));
        entity.addComponent(TestComponentIds.Motion, new Motion(100, 100));

    }


    @Test
    public void OnComponentReplacedTest() {
        entity.OnComponentReplaced((IEntity e, int index, IComponent c, IComponent n)
                -> assertEquals(33F, ((Position) n).x, 0.1f));
        entity.replaceComponent(TestComponentIds.Position, new Position(33, 100));

    }

    @Test
    public void OnComponentReplaced2Test() {
        entity.OnComponentReplaced((IEntity e, int index, IComponent c, IComponent n)
                -> assertEquals(100F, ((Position) n).x, 0.1f));
        entity.replaceComponent(TestComponentIds.Position, entity.getComponent(TestComponentIds.Position));

    }


    @Test
    public void OnComponentRemovedTest() {
        entity.OnComponentRemoved((IEntity e, int index, IComponent c)
                -> assertFalse(e.hasComponent(index)));
        entity.removeComponent(TestComponentIds.View);

    }

    @Test
    public void removeComponentTest() {
        entity.removeComponent(TestComponentIds.Position);
        assertEquals(false, entity.hasComponent(1));
    }

    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void removeComponentExceptionTest() {
        entity.removeComponent(TestComponentIds.Motion);

    }

    @Test
    public void replaceComponentTest() {
        entity.replaceComponent(TestComponentIds.Position, new Position(50F, 50F));
        assertEquals(50F, ((Position) entity.getComponent(TestComponentIds.Position)).x, 0.1F);
    }

    @Test
    public void replaceNotExistComponentTest() {
        entity.replaceComponent(TestComponentIds.Motion, new Motion(50F, 50F));
        assertEquals(50F, ((Motion) entity.getComponent(TestComponentIds.Motion)).x, 0.1F);
    }

    @Test
    public void falseReplaceComponentTest() {
        entity.replaceComponent(TestComponentIds.Motion, new Motion(50F, 50F));
        assertNotEquals(100F, ((Motion) entity.getComponent(TestComponentIds.Motion)).x, 0.1F);
    }

    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException() {
        entity.getComponent(TestComponentIds.Score);

    }


    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException3() {
        entity.removeComponent(TestComponentIds.Score);

    }

    @Test
    public void getComponents() {
        assertEquals(2, entity.getComponents().length);

    }

    @Test
    public void createComponent() throws InstantiationException, IllegalAccessException {
        Interactive component = (Interactive) entity.createComponent(TestComponentIds.Interactive);
        assertNotNull(component);

    }

    @Test
    public void createComponentFromPool() throws InstantiationException, IllegalAccessException {
        Interactive component = (Interactive) entity.createComponent(TestComponentIds.Interactive);
        entity.addComponent(TestComponentIds.Interactive, component);
        entity.removeComponent(TestComponentIds.Interactive);
        component = (Interactive) entity.createComponent(TestComponentIds.Interactive);
        assertNotNull(component);

    }

    @Test
    public void recoverComponent() throws InstantiationException, IllegalAccessException {
        Interactive component = (Interactive) entity.createComponent(TestComponentIds.Interactive);
        entity.addComponent(TestComponentIds.Interactive, component);
        entity.removeComponent(TestComponentIds.Interactive);
        component = (Interactive) entity.recoverComponent(TestComponentIds.Interactive);
        assertNotNull(component);

    }

    @Test
    public void recoverNullComponent() throws InstantiationException, IllegalAccessException {
        Position component = (Position) entity.recoverComponent(TestComponentIds.Position);
        assertNull(component);

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled() {
        entity.destroy();
        entity.addComponent(TestComponentIds.Position, new Position(100, 100));

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled2() {
        entity.destroy();
        entity.removeComponent(TestComponentIds.Position);

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled3() {
        entity.destroy();
        entity.replaceComponent(TestComponentIds.Position, new Position(100, 100));

    }


    @Test
    public void getIndices() {
        int[] indices = entity.getComponentIndices();
        assertTrue(entity.hasComponents(indices));

    }

    @Test
    public void falseHasComponentsTest() {
        int[] indices = entity.getComponentIndices();
        indices[0] = TestComponentIds.Score;
        assertFalse(entity.hasComponents((int[]) indices));

    }

    @Test
    public void removeAllComponentsTest() {
        entity.removeAllComponents();
        assertEquals(0, entity.getComponents().length);

    }

    @Test
    public void destroyTest() {
        assertTrue(entity.isEnabled());
        entity.destroy();
        entity.getCreationIndex();
        assertFalse(entity.isEnabled());
    }

    @Test
    public void retainTest() {
        entity.retain(new Object());
        assertEquals(1, entity.retainCount());
    }


    @Test
    public void releaseTest() {
        Object owner = new Object();
        entity.OnEntityReleased((IEntity e) -> assertEquals(0, e.retainCount()));
        entity.retain(owner);
        entity.release(owner);

    }


}


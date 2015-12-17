package com.ilargia.games.entitas;

import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.IComponent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class EntityTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Entity entity;


    @Before
    public void setUp() throws Exception {
        entity = new Entity(100);
        entity.setCreationIndex(1);
        entity.addComponent(1, new Position(100, 100));
        entity.addComponent(2, new Position(100, 100));

    }

    @Test
    public void EntityHashComponent() {
        assertEquals(true, entity.hasComponent(1));

    }

    @Test
    public void EntityDoesNotHaveComponent() {
        assertEquals(false, entity.hasComponent(31));

    }

    @Test
    public void EntityHasSomeComponent() {
        List<Integer> indices = new ArrayList<Integer>();
        indices.add(1);
        indices.add(33);

        assertEquals(true, entity.hasAnyComponent(indices));

    }

    @Test
    public void EntityDoesNotHaveAnyComponent() {
        List<Integer> indices = new ArrayList<Integer>();
        indices.add(11);
        indices.add(33);

        assertEquals(false, entity.hasAnyComponent(indices));

    }

    @Test(expected = EntityAlreadyHasComponentException.class)
    public void EntityAlreadyHasComponentException() {
        entity.addComponent(1, new Position(100, 100));

    }

    @Test
    public void OnComponentAddedTest() {
        entity.OnComponentAdded.addListener((Entity e,int index, IComponent c) -> assertEquals(55, index));
        entity.addComponent(55, new Position(100, 100));

    }


    @Test
    public void OnComponentReplacedTest() {
        entity.OnComponentReplaced.addListener((Entity e, int index, IComponent c, IComponent n)
                -> assertEquals(33F, ((Position)n).getX(), 0.1f));
        entity.replaceComponent(1, new Position(33, 100));

    }

    @Test
    public void OnComponentReplaced2Test() {
        entity.OnComponentReplaced.addListener((Entity e, int index, IComponent c, IComponent n)
                -> assertEquals(100F, ((Position)n).getX(), 0.1f));
        entity.replaceComponent(1, entity.getComponent(1));

    }


    @Test
    public void OnComponentRemovedTest() {
        entity.OnComponentRemoved.addListener((Entity e, int index, IComponent c)
                                                -> assertFalse(e.hasComponent(index)));
        entity.removeComponent(1);

    }

    @Test
    public void removeComponentTest() {
        entity.removeComponent(1);
        assertEquals(false, entity.hasComponent(1));
    }

    @Test
    public void replaceComponentTest() {
        entity.replaceComponent(1, new Position(50F, 50F));
        assertEquals(50F, entity.<Position>getComponent(1).getX(),0.1F);
    }

    @Test
    public void replaceNotExistComponentTest() {
        entity.replaceComponent(99, new Position(50F, 50F));
        assertEquals(50F, entity.<Position>getComponent(99).getX(),0.1F);
    }

    @Test
    public void falseReplaceComponentTest() {
        entity.replaceComponent(1, new Position(50F,50F));
        assertNotEquals(100F, entity.<Position>getComponent(1).getX(), 0.1F);
    }

    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException() {
        entity.<Position>getComponent(99);

    }

    @Test
    public void EntityDoesNotHaveComponentException2() {
        exception.expect(EntityDoesNotHaveComponentException.class);
        exception.expectMessage("Cannot add component at index 100; Max index 100\n" +
                "Entity does not have a component at index 100");

        entity.addComponent(100, new Position(100, 100));

    }

    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException3() {
        entity.removeComponent(100);

    }

    @Test
    public void getComponents() {
        assertEquals(2, entity.getComponents().length);

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled() {
        entity.setEnabled(false);
        entity.addComponent(5, new Position(100, 100));

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled2() {
        entity.setEnabled(false);
        entity.removeComponent(1);

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled3() {
        entity.setEnabled(false);
        entity.replaceComponent(1, new Position(100, 100));

    }


    @Test
    public void getIndices() {
        List<Integer> indices = entity.getComponentIndices();
        assertTrue(entity.hasComponents(indices));

    }

    @Test
    public void falseHasComponentsTest() {
        List<Integer> indices = entity.getComponentIndices();
        indices.add(33);
        assertFalse(entity.hasComponents(indices));

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
        assertEquals(1, entity.getRetainCount());
    }


    @Test
    public void releaseTest() {
        Object owner = new Object();
        entity.OnEntityReleased.addListener((Entity e)-> assertEquals(0, e.getRetainCount()));
        entity.retain(owner);
        entity.release(owner);

    }

    @Test(expected = EntityIsNotRetainedByOwnerException.class)
    public void EntityIsNotRetainedByOwnerExceptionTest() {
        Object owner = new Object();
        entity.release(owner);
    }

    @Test(expected = EntityIsAlreadyRetainedByOwnerException.class)
    public void EntityIsAlreadyRetainedByOwnerExceptionTest() {
        Object owner = new Object();
        entity.retain(owner);
        entity.retain(owner);
    }

}


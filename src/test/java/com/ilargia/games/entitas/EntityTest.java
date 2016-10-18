package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.IntArray;
import com.ilargia.games.entitas.components.Movable;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.Views;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.IComponent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;

public class EntityTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Entity entity;


    @Before
    public void setUp() throws Exception {
        IComponent.registerComponent(Position.class);
        IComponent.registerComponent(Movable.class);
        IComponent.registerComponent(Views.class);
        IComponent.registerComponent(Views.class);

        entity = new Entity(IComponent.getComponentSize());
        entity.setCreationIndex(1);
        entity.addComponent(new Position(100, 100));
        entity.addComponent(new Views());


    }

    @Test
    public void componentSize() {
        assertEquals(3, IComponent.getComponentSize());


    }

    @Test
    public void componentTypes() {
        assertEquals(0,  IComponent.getIdComponent(Position.class));
        assertEquals(1,  IComponent.getIdComponent(Movable.class));
        assertEquals(2, IComponent.getIdComponent(Views.class));

    }

    @Test
    public void EntityHashComponent() {
        assertEquals(true, entity.hasComponent(2));

    }

    @Test
    public void EntityDoesNotHaveComponent() {
        assertEquals(false, entity.hasComponent(1));

    }

    @Test
    public void EntityHasSomeComponent() {
        IntArray indices = new IntArray();
        indices.add(1);
        indices.add(2);

        assertEquals(true, entity.hasAnyComponent(indices));

    }

    @Test
    public void EntityDoesNotHaveAnyComponent() {
        IntArray indices = new IntArray();
        indices.add(1);

        assertEquals(false, entity.hasAnyComponent(indices));

    }

    @Test(expected = EntityAlreadyHasComponentException.class)
    public void EntityAlreadyHasComponentException() {
        entity.addComponent(new Position(100, 100));

    }

    @Test
    public void OnComponentAddedTest() {
        entity.OnComponentAdded.addListener((Entity e,int index, IComponent c) -> assertEquals(55, index));
        entity.addComponent(new Position(100, 100));

    }


    @Test
    public void OnComponentReplacedTest() {
        entity.OnComponentReplaced.addListener((Entity e, int index, IComponent c, IComponent n)
                -> assertEquals(33F, ((Position)n).getX(), 0.1f));
        entity.replaceComponent(new Position(33, 100));

    }

    @Test
    public void OnComponentReplaced2Test() {
        entity.OnComponentReplaced.addListener((Entity e, int index, IComponent c, IComponent n)
                -> assertEquals(100F, ((Position)n).getX(), 0.1f));
        entity.replaceComponent(entity.getComponent(Position.class));

    }


    @Test
    public void OnComponentRemovedTest() {
        entity.OnComponentRemoved.addListener((Entity e, int index, IComponent c)
                                                -> assertFalse(e.hasComponent(index)));
        entity.removeComponent(2);

    }

    @Test
    public void removeComponentTest() {
        entity.removeComponent(1);
        assertEquals(false, entity.hasComponent(1));
    }

    @Test
    public void replaceComponentTest() {
        entity.replaceComponent(new Position(50F, 50F));
        assertEquals(50F, entity.getComponent(Position.class).getX(),0.1F);
    }

    @Test
    public void replaceNotExistComponentTest() {
        entity.replaceComponent(new Position(50F, 50F));
        assertEquals(50F, entity.getComponent(Position.class).getX(),0.1F);
    }

    @Test
    public void falseReplaceComponentTest() {
        entity.replaceComponent(new Position(50F,50F));
        assertNotEquals(100F, entity.getComponent(Position.class).getX(), 0.1F);
    }

    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException() {
        entity.getComponent(Movable.class);

    }

    @Test
    public void EntityDoesNotHaveComponentException2() {
        exception.expect(EntityDoesNotHaveComponentException.class);
        exception.expectMessage("Cannot add component at index 33; Max index 3" );

        entity.addComponent(33,null);

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
        entity.addComponent(new Position(100, 100));

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled2() {
        entity.setEnabled(false);
        entity.removeComponent(1);

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled3() {
        entity.setEnabled(false);
        entity.replaceComponent(new Position(100, 100));

    }


   // @Test
    public void getIndices() {
        int[] indices = entity.getComponentIndices();
        assertTrue(entity.hasComponents(indices));

    }

    @Test
    public void falseHasComponentsTest() {
        int[] indices = entity.getComponentIndices();
        indices[0] =1;
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


package com.ilargia.games.entitas;

import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException;
import com.ilargia.games.entitas.exceptions.EntityDoesNotHaveComponentException;
import com.ilargia.games.entitas.exceptions.EntityIsNotEnabledException;
import com.ilargia.games.entitas.interfaces.IComponent;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Stack;

import static org.junit.Assert.*;

public class EntityTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Entity entity;
    private Stack<IComponent>[] _componentPools;


    @Before
    public void setUp() throws Exception {
        _componentPools = new Stack[10];
        EntitasCache cache = new EntitasCache();
        entity = new Entity(10, _componentPools, null);
        entity.setCreationIndex(0);
        entity.addComponent(1, new Position(100, 100));
        entity.addComponent(2, new Views());


    }

    //@Test
    public void componentSize() {
        //assertEquals(3, IComponent.getComponentSize());


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
        assertEquals(true, entity.hasComponent(2));

    }

    @Test
    public void EntityDoesNotHaveComponent() {
        assertEquals(false, entity.hasComponent(3));

    }

    @Test
    public void EntityHasSomeComponent() {
        assertEquals(true, entity.hasAnyComponent(1, 3));
    }

    @Test
    public void EntityDoesNotHaveAnyComponent() {
        assertEquals(false, entity.hasAnyComponent(3));

    }

    @Test(expected = EntityAlreadyHasComponentException.class)
    public void EntityAlreadyHasComponentException() {
        entity.addComponent(1, new Position(100, 100));

    }

    @Test
    public void OnComponentAddedTest() {
        entity.OnComponentAdded = ((Entity e, int index, IComponent c) -> assertEquals(3, index));
        entity.addComponent(3, new Position(100, 100));

    }


    @Test
    public void OnComponentReplacedTest() {
        entity.OnComponentReplaced = ((Entity e, int index, IComponent c, IComponent n)
                -> assertEquals(33F, ((Position) n).getX(), 0.1f));
        entity.replaceComponent(1, new Position(33, 100));

    }

    @Test
    public void OnComponentReplaced2Test() {
        entity.OnComponentReplaced = ((Entity e, int index, IComponent c, IComponent n)
                -> assertEquals(100F, ((Position) n).getX(), 0.1f));
        entity.replaceComponent(1, entity.getComponent(1));

    }


    @Test
    public void OnComponentRemovedTest() {
        entity.OnComponentRemoved = ((Entity e, int index, IComponent c)
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
        entity.replaceComponent(1, new Position(50F, 50F));
        assertEquals(50F, ((Position) entity.getComponent(1)).getX(), 0.1F);
    }

    @Test
    public void replaceNotExistComponentTest() {
        entity.replaceComponent(1, new Position(50F, 50F));
        assertEquals(50F, ((Position) entity.getComponent(1)).getX(), 0.1F);
    }

    @Test
    public void falseReplaceComponentTest() {
        entity.replaceComponent(1, new Position(50F, 50F));
        assertNotEquals(100F, ((Position) entity.getComponent(1)).getX(), 0.1F);
    }

    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException() {
        entity.getComponent(3);

    }


    @Test(expected = EntityDoesNotHaveComponentException.class)
    public void EntityDoesNotHaveComponentException3() {
        entity.removeComponent(100);

    }

    @Test
    public void getComponents() {
        assertEquals(2, entity.getComponents().length);

    }

    @Test
    public void createComponent() throws InstantiationException, IllegalAccessException {
        entity.createComponent(1);

    }

    @Test(expected = EntityIsNotEnabledException.class)
    public void notEnabled() {
        entity.setEnabled(false);
        entity.addComponent(1, new Position(100, 100));

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


    // @Test
    public void getIndices() {
        Integer[] indices = entity.getComponentIndices();
        assertTrue(entity.hasComponents(indices));

    }

    @Test
    public void falseHasComponentsTest() {
        Integer[] indices = entity.getComponentIndices();
        indices[0] = 3;
        assertFalse(entity.hasComponents((Integer[]) indices));

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
        entity.OnEntityReleased = ((Entity e) -> assertEquals(0, e.getRetainCount()));
        entity.retain(owner);
        entity.release(owner);

    }


}


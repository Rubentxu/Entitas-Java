package com.ilargia.games.entitas;

import com.ilargia.games.entitas.components.Position;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by rdcabrera on 16/12/2015.
 */
public class EntityTest {


    @Test
    public void EntityHashComponent() {
        Entity entity = new Entity(100);
        entity.addComponent(1, new Position(100, 100));

        assertEquals(true, entity.hasComponent(1));

    }

    @Test
    public void EntityNotHashComponent() {
        Entity entity = new Entity(100);

        assertEquals(false, entity.hasComponent(1));

    }

    @Test
    public void EntityHasAnyComponent() {
        Entity entity = new Entity(100);
        entity.addComponent(1, new Position(100, 100));
        entity.addComponent(2, new Position(100, 100));

        List<Integer> indices = new ArrayList<Integer>();
        indices.add(1);
        indices.add(33);

        assertEquals(true, entity.hasAnyComponent(indices));

    }

    @Test
    public void EntityNotHasAnyComponent() {
        Entity entity = new Entity(100);
        entity.addComponent(1, new Position(100, 100));
        entity.addComponent(2, new Position(100, 100));

        List<Integer> indices = new ArrayList<Integer>();
        indices.add(11);
        indices.add(33);

        assertEquals(false, entity.hasAnyComponent(indices));

    }

}

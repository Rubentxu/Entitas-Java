package com.ilargia.games.entitas;

import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.View;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class GroupTest {

    private Group group;
    private Entity entity;
    private Group group2;
    private EventBus<Entity> bus;


    private void createCollections() {
        new Collections(new CollectionsFactory() {
            @Override
            public List createList() {
                return new ArrayList();
            }

            @Override
            public Set createSet() {
                return new HashSet();
            }

            @Override
            public Map createMap() {
                return new HashMap();
            }
        });
    }


    @Before
    public void setUp() throws Exception {
        createCollections();
        bus = new EventBus<>();
        entity = new Entity(10, new Stack[10], new EntityMetaData("Test", TestComponentIds.componentNames(),
                TestComponentIds.componentTypes()), bus);
        entity.setCreationIndex(0);
        entity.addComponent(TestComponentIds.Position, new Position(100, 100));
        entity.addComponent(TestComponentIds.View, new View(1));

        group = new Group(TestMatcher.Position(), Entity.class);
        group2 = new Group(TestMatcher.Interactive(), Entity.class);
        new Collections(new CollectionsFactory() {
            @Override
            public List createList() {
                return new ArrayList();
            }

            @Override
            public Set createSet() {
                return new HashSet();
            }

            @Override
            public Map createMap() {
                return new HashMap();
            }
        });
    }

    @Test
    public void handleEntityTest() {
        group.OnEntityAdded = (group, e, idx, component) -> assertEquals(entity, e);
        GroupChanged changed = group.handleEntity(entity);
        assertEquals(group.OnEntityAdded, changed);

    }

    @Test
    public void handleEntityOnEntityRemovedTest() {
        group.OnEntityRemoved = (group, e, idx, component) -> assertEquals(entity, e);
        group.handleEntity(entity);

        entity.removeComponent(TestComponentIds.Position);
        GroupChanged changed = group.handleEntity(entity);

        assertEquals(group.OnEntityRemoved, changed);

    }

    @Test
    public void handleEntitySilentlyTest() {
        group.handleEntitySilently(entity);
        assertEquals(1, group.getEntities().length);

    }

    @Test
    public void handleEntitySilentlyOnEntityRemovedTest() {
        group.handleEntitySilently(entity);
        assertEquals(1, group.getEntities().length);

        entity.removeComponent(TestComponentIds.Position);
        group.handleEntitySilently(entity);
        assertFalse(group.containsEntity(entity));

    }


    @Test
    public void getSingleEntityTest() {
        assertNull(group.getSingleEntity());
        group.handleEntitySilently(entity);

        assertEquals(entity, group.getSingleEntity());

    }


}

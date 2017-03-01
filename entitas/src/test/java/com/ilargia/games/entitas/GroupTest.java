package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.View;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestEntity;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import static org.junit.Assert.*;

public class GroupTest {

    private Group<TestEntity> group;
    private TestEntity entity;
    private Group group2;


    private void createCollections() {
        new EntitasCollections(new CollectionsFactories(){});
    }


    @Before
    public void setUp() throws Exception {
        createCollections();

        entity = new TestEntity();
        entity.initialize(0,10, new Stack[10], new ContextInfo("Test", TestComponentIds.componentNames(),
                TestComponentIds.componentTypes()));
        entity.clearEventsListener();
        entity.reactivate(0);
        entity.addComponent(TestComponentIds.Position, new Position(100, 100));
        entity.addComponent(TestComponentIds.View, new View(1));

        group = new Group<TestEntity>(TestMatcher.Position(), TestEntity.class);
        group2 = new Group(TestMatcher.Interactive(), Entity.class);

    }

    @Test
    public void handleEntityTest() {
        GroupChanged<TestEntity> lambda = (group, e, index, component) -> {
            entityEquals(entity, e);
        };
        group.OnEntityAdded(lambda);
        Set<GroupChanged> changed = group.handleEntity(entity);
        assertTrue(changed.contains(lambda));

    }

    private void entityEquals(TestEntity entity, Object entity2) {
        assertEquals(entity, entity2);
    }

    @Test
    public void handleEntityOnEntityRemovedTest() {
        GroupChanged<TestEntity> lambda = (group, e, idx, component) -> assertEquals(entity, e);
        group.OnEntityRemoved(lambda);
        Set<GroupChanged> changed = group.handleEntity(entity);
        assertEquals(0, changed.size());

        entity.removeComponent(TestComponentIds.Position);
        changed = group.handleEntity(entity);
        assertTrue(changed.contains(lambda));


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

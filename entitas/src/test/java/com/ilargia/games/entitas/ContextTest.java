package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.*;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.index.PrimaryEntityIndex;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestContext;
import com.ilargia.games.entitas.utils.TestEntity;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.*;

public class ContextTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private TestContext context;
    private TestEntity entity;


    public FactoryEntity<TestEntity> factoryEntity() {
        return (int totalComponents, Stack<IComponent>[] componentPools,
                ContextInfo contextInfo) -> {
            return new TestEntity(totalComponents, componentPools, contextInfo);
        };
    }

    public TestContext createTestPool() {
        return new TestContext(TestComponentIds.totalComponents, 0,
                new ContextInfo("Test", TestComponentIds.componentNames(),
                        TestComponentIds.componentTypes()), factoryEntity());
    }


    private void createCollections() {
        new Collections(new CollectionsFactory() {
            @Override
            public List createList(Class<?> clazz) {
                return new ArrayList();
            }

            @Override
            public Set createSet(Class<?> clazz) {
                return new HashSet();
            }

            @Override
            public Map createMap(Class<?> keyClazz, Class<?> valueClazz) {
                return new HashMap();
            }

        });
    }

    @Before
    public void setUp() throws Exception {
        createCollections();
        context = createTestPool();
        entity = context.createEntity();
        entity.clearEventsListener();
        context.clearEventsListener();


    }

    @Test
    public void OnEntityCreatedTest() {
        context.OnEntityCreated((context, e) -> assertTrue(e.isEnabled()));
        entity = context.createEntity();
    }

    @Test
    public void hasEntityTest() {
        assertTrue(context.hasEntity(entity));

    }

    @Test
    public void getCountTest() {
        assertEquals(1, context.getCount());

    }

    @Test
    public void getEntitiesTest() {
        assertEquals(1, context.getEntities().length);

    }

    @Test
    public void destroyEntityTest() {
        context.destroyEntity(entity);
        assertEquals(0, context.getEntities().length);

    }

    @Test
    public void OnEntityDestroyedTest() {
        context.OnEntityWillBeDestroyed((IContext pool, IEntity e) -> assertTrue(e.isEnabled()));
        context.OnEntityDestroyed((pool, e) -> assertFalse(e.isEnabled()));
        context.destroyAllEntities();
        assertEquals(0, context.getCount());

    }

    @Test
    public void getReusableEntitiesCountTest() {
        TestEntity entity2 = context.createEntity();
        assertEquals(0, context.getReusableEntitiesCount());
        context.destroyEntity(entity2);
        assertEquals(2, context.getReusableEntitiesCount());

    }

    @Test
    public void getRetainedEntitiesCountTest() {
        TestEntity entity2 = context.createEntity();
        entity.retain(new Object());
        context.destroyEntity(entity);
        assertEquals(1, context.getRetainedEntitiesCount());
        context.destroyEntity(entity2);
        assertEquals(1, context.getRetainedEntitiesCount());

    }

    @Test(expected = ContextStillHasRetainedEntitiesException.class)
    public void PoolStillHasRetainedEntitiesExceptionTest() {
        entity.retain(new Object());
        context.destroyAllEntities();


    }

    @Test(expected = EntityIsNotRetainedByOwnerException.class)
    public void entityIsNotRetainedByOwnerExceptionTest() {
        TestEntity entity2 = new TestEntity(100, null, null);
        context.destroyEntity(entity2);

    }

    @Test
    public void onEntityReleasedTest() {
        context.destroyEntity(entity);


        assertEquals(1, context.getReusableEntitiesCount());
    }

    @Test
    public void getGroupTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = context.getGroup(TestMatcher.Position());
        assertEquals(1, group.getCount());
        group = context.getGroup(TestMatcher.Position());
        assertEquals(1, group.getCount());
    }

    @Test
    public void getGroupEntitiesTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = context.getGroup(TestMatcher.Position());
        assertEquals(1, group.getEntities().length);
    }

    @Test
    public void clearGroupsTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = context.getGroup(TestMatcher.Position());
        context.clearGroups();

    }

    @Test
    public void entityIndexTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = context.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<Entity, String> index = new PrimaryEntityIndex<>(group, (e, c) -> "positionEntities");
        context.addEntityIndex("positions", index);
        index = (PrimaryEntityIndex<Entity, String>) context.getEntityIndex("positions");
        assertNotNull(index);
        assertTrue(index.hasEntity("positionEntities"));

    }

    @Test(expected = ContextEntityIndexDoesAlreadyExistException.class)
    public void duplicateEntityIndexTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = context.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<Entity, String> index = new PrimaryEntityIndex<>(group, (e, c) -> "positionEntities");
        context.addEntityIndex("duplicate", index);
        context.addEntityIndex("duplicate", index);

    }


    @Test(expected = ContextEntityIndexDoesNotExistException.class)
    public void deactivateAndRemoveEntityIndicesTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = context.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<Entity, String> index = new PrimaryEntityIndex<>(group, (e, c) -> "positionEntities");
        context.addEntityIndex("positions", index);

        context.deactivateAndRemoveEntityIndices();
        index = (PrimaryEntityIndex<Entity, String>) context.getEntityIndex("positions");


    }

    @Test
    public void clearComponentPoolTest() {
        Stack[] cpool = context.getComponentPools();
        cpool[0] = new Stack<IComponent>();
        cpool[0].push(new Position());

        assertEquals(1, cpool[0].size());
        context.clearComponentPool(0);

        assertTrue(cpool[0].empty());

    }

    @Test
    public void clearComponentPoolsTest() {
        Stack[] cpool = context.getComponentPools();
        cpool[0] = new Stack<IComponent>();
        cpool[0].push(new Position());

        assertEquals(1, cpool[0].size());
        context.clearComponentPools();

        assertTrue(cpool[0].empty());

    }

    @Test
    public void resetTest() {
        context.OnEntityCreated((pool, entity) -> {
        });
        assertEquals(1, context.OnEntityCreated.get(context).size());
        context.reset();
        assertEquals(0, context.OnEntityCreated.get(context).size());

    }

    @Test
    public void updateGroupsComponentAddedOrRemovedTest() {
        Position position = new Position();
        Group<TestEntity> group = context.getGroup(TestMatcher.Position());
        group.OnEntityAdded((g, e, idx, pc) -> assertEquals(TestComponentIds.Position, idx));

        entity.addComponent(TestComponentIds.Position, position);
        context.updateGroupsComponentAddedOrRemoved(entity, TestComponentIds.Position, position, context._groupsForIndex);
        context.updateGroupsComponentAddedOrRemoved(entity, TestComponentIds.Position, position, context._groupsForIndex);
        //context.OnGroupCleared = (context, group)->  assertNull(context.OnEntityCreated);

    }

    @Test
    public void updateGroupsComponentReplacedTest() {
        Position position = new Position();
        Position position2 = new Position();
        Group<TestEntity> groupE = context.getGroup(TestMatcher.Position());
        groupE.OnEntityUpdated((IGroup<TestEntity> group, final TestEntity entity, int index, IComponent previousComponent, IComponent nc) -> {
            System.out.println("Removed...........");
            assertEquals(position2, nc);
        });

        entity.addComponent(TestComponentIds.Position, position);
        context.updateGroupsComponentReplaced(entity, TestComponentIds.Position, position, position2, context._groupsForIndex);

    }

    @Test
    public void createEntityCollectorTest() {
        Context[] pools = new Context[]{context};
        Collector collector = context.createEntityCollector(pools, TestMatcher.Position());
        assertNotNull(collector);

    }


}

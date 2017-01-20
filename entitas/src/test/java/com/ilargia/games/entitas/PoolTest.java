package com.ilargia.games.entitas;

import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.entitas.interfaces.FactoryEntity;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.*;

public class PoolTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private BasePool pool;
    private Entity entity;
    private EventBus<Entity> bus;


    public FactoryEntity<Entity> factoryEntity() {
        return (int totalComponents, Stack<IComponent>[] componentPools,
                EntityMetaData entityMetaData) -> {
            return new Entity(totalComponents, componentPools, entityMetaData, bus);
        };
    }

    public BasePool createTestPool() {
        return new BasePool(TestComponentIds.totalComponents, 0,
                new EntityMetaData("Test", TestComponentIds.componentNames(),
                        TestComponentIds.componentTypes()), bus, factoryEntity());
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
        bus = new EventBus<>();
        pool = createTestPool();
        entity = pool.createEntity();


    }

    @Test
    public void OnEntityCreatedTest() {
        bus.OnEntityCreated.addListener((BasePool pool, Entity e) -> assertTrue(e.isEnabled()));
        entity = pool.createEntity();
    }

    @Test
    public void hasEntityTest() {
        assertTrue(pool.hasEntity(entity));

    }

    @Test
    public void getCountTest() {
        assertEquals(1, pool.getCount());

    }

    @Test
    public void getEntitiesTest() {
        assertEquals(1, pool.getEntities().length);

    }

    @Test
    public void destroyEntityTest() {
        pool.destroyEntity(entity);
        assertEquals(0, pool.getEntities().length);

    }

    @Test
    public void OnEntityDestroyedTest() {
        bus.OnEntityWillBeDestroyed.addListener((BasePool pool, Entity e) -> assertTrue(e.isEnabled()));
        bus.OnEntityDestroyed.addListener((BasePool pool, Entity e) -> assertFalse(e.isEnabled()));
        pool.destroyAllEntities();
        assertEquals(0, pool.getCount());

    }

    @Test
    public void getReusableEntitiesCountTest() {
        Entity entity2 = pool.createEntity();
        assertEquals(0, pool.getReusableEntitiesCount());
        pool.destroyEntity(entity2);
        assertEquals(1, pool.getReusableEntitiesCount());

    }

    @Test
    public void getRetainedEntitiesCountTest() {
        Entity entity2 = pool.createEntity();
        entity.retain(new Object());
        pool.destroyEntity(entity);
        assertEquals(1, pool.getRetainedEntitiesCount());
        pool.destroyEntity(entity2);
        assertEquals(1, pool.getRetainedEntitiesCount());

    }

    @Test(expected = PoolStillHasRetainedEntitiesException.class)
    public void PoolStillHasRetainedEntitiesExceptionTest() {
        entity.retain(new Object());
        pool.destroyAllEntities();


    }

    @Test(expected = PoolDoesNotContainEntityException.class)
    public void PoolDoesNotContainEntityExceptionTest() {
        Entity entity2 = new Entity(100, null, null, null);
        pool.destroyEntity(entity2);

    }

    @Test
    public void onEntityReleasedTest() {
        entity.destroy();
        entity.release(pool);

        assertEquals(1, pool.getReusableEntitiesCount());
    }

    @Test(expected = EntityIsNotDestroyedException.class)
    public void EntityIsNotDestroyedExceptionTest() {
        entity.release(pool);
    }

    @Test
    public void getGroupTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        assertEquals(1, group.getcount());
        group = pool.getGroup(TestMatcher.Position());
        assertEquals(1, group.getcount());
    }

    @Test
    public void getGroupEntitiesTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        assertEquals(1, group.getEntities().length);
    }

    @Test
    public void clearGroupsTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        pool.clearGroups();

    }

    @Test
    public void entityIndexTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<String, Entity> index = new PrimaryEntityIndex<>(group, (e, c) -> "positionEntities");
        pool.addEntityIndex("positions", index);
        index = (PrimaryEntityIndex<String, Entity>) pool.getEntityIndex("positions");
        assertNotNull(index);
        assertTrue(index.hasEntity("positionEntities"));

    }

    @Test(expected = PoolEntityIndexDoesAlreadyExistException.class)
    public void duplicateEntityIndexTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<String, Entity> index = new PrimaryEntityIndex<>(group, (e, c) -> "positionEntities");
        pool.addEntityIndex("duplicate", index);
        pool.addEntityIndex("duplicate", index);

    }


    @Test(expected = PoolEntityIndexDoesNotExistException.class)
    public void deactivateAndRemoveEntityIndicesTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<String, Entity> index = new PrimaryEntityIndex<>(group, (e, c) -> "positionEntities");
        pool.addEntityIndex("positions", index);

        pool.deactivateAndRemoveEntityIndices();
        index = (PrimaryEntityIndex<String, Entity>) pool.getEntityIndex("positions");


    }

    @Test
    public void clearComponentPoolTest() {
        Stack[] cpool = pool.getComponentPools();
        cpool[0] = new Stack<IComponent>();
        cpool[0].push(new Position());

        assertEquals(1, cpool[0].size());
        pool.clearComponentPool(0);

        assertTrue(cpool[0].empty());

    }

    @Test
    public void clearComponentPoolsTest() {
        Stack[] cpool = pool.getComponentPools();
        cpool[0] = new Stack<IComponent>();
        cpool[0].push(new Position());

        assertEquals(1, cpool[0].size());
        pool.clearComponentPools();

        assertTrue(cpool[0].empty());

    }

    @Test
    public void resetTest() {
        bus.OnEntityCreated.addListener((pool, entity) -> {
        });
        assertEquals(1, bus.OnEntityCreated.listeners().size());
        pool.reset();
        assertEquals(0, bus.OnEntityCreated.listeners().size());

    }

    @Test
    public void updateGroupsComponentAddedOrRemovedTest() {
        Position position = new Position();
        Group group = pool.getGroup(TestMatcher.Position());
        group.OnEntityAdded = (g, e, idx, pc) -> assertEquals(TestComponentIds.Position, idx);

        entity.addComponent(TestComponentIds.Position, position);
        pool.updateGroupsComponentAddedOrRemoved(entity, TestComponentIds.Position, position, pool._groupsForIndex);
        pool.updateGroupsComponentAddedOrRemoved(entity, TestComponentIds.Position, position, pool._groupsForIndex);
        //pool.OnGroupCleared = (pool, group)->  assertNull(pool.OnEntityCreated);

    }

    @Test
    public void updateGroupsComponentReplacedTest() {
        Position position = new Position();
        Position position2 = new Position();
        Group group = pool.getGroup(TestMatcher.Position());
        group.OnEntityUpdated = (g, e, idx, pc, nc) -> {
            System.out.println("OnEntityRemoved...........");
            assertEquals(position2, nc);
        };

        entity.addComponent(TestComponentIds.Position, position);
        pool.updateGroupsComponentReplaced(entity, TestComponentIds.Position, position, position2, pool._groupsForIndex);

    }

    @Test
    public void createEntityCollectorTest() {
        BasePool[] pools = new BasePool[]{pool};
        EntityCollector collector = BasePool.createEntityCollector(pools, TestMatcher.Position());
        assertNotNull(collector);

    }


}

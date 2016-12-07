package com.ilargia.games.entitas;

import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.exceptions.*;
import com.ilargia.games.entitas.interfaces.FactoryEntity;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Stack;

import static org.junit.Assert.*;

public class PoolTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private BasePool pool;
    private Entity entity;


    public FactoryEntity<Entity> factoryEntity() {
        return (int totalComponents, Stack<IComponent>[] componentPools,
                EntityMetaData entityMetaData) -> {
            return new Entity(totalComponents, componentPools, entityMetaData);
        };
    }

    public BasePool createTestPool() {
        return new BasePool(TestComponentIds.totalComponents, 0,
                new EntityMetaData("Test", TestComponentIds.componentNames(),
                        TestComponentIds.componentTypes()), factoryEntity());
    }

    @Before
    public void setUp() throws Exception {
        pool = createTestPool();
        entity = pool.createEntity();

    }

    @Test
    public void OnEntityCreatedTest() {
        pool.OnEntityCreated = ((BasePool pool, Entity e) -> assertTrue(e.isEnabled()));
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
        pool.OnEntityWillBeDestroyed = ((BasePool pool, Entity e) -> assertTrue(e.isEnabled()));
        pool.OnEntityDestroyed = ((BasePool pool, Entity e) -> assertFalse(e.isEnabled()));
        pool.destroyAllEntities();
        assertEquals(0, pool.getCount());

    }

    @Test
    public void getReusableEntitiesCountTest() {
        Entity entity2 = pool.createEntity();
        pool.destroyEntity(entity2);
        assertEquals(1, pool.getReusableEntitiesCount());

    }

    @Test
    public void getRetainedEntitiesCountTest() {
        entity.retain(new Object());
        pool.destroyEntity(entity);
        assertEquals(1, pool.getRetainedEntitiesCount());

    }

    @Test(expected = PoolStillHasRetainedEntitiesException.class)
    public void PoolStillHasRetainedEntitiesExceptionTest() {
        entity.retain(new Object());
        pool.destroyAllEntities();


    }

    @Test(expected = PoolDoesNotContainEntityException.class)
    public void PoolDoesNotContainEntityExceptionTest() {
        Entity entity2 = new Entity(100, null, null);
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
        assertEquals(1, group.getcount() );
        group = pool.getGroup(TestMatcher.Position());
        assertEquals(1, group.getcount() );
    }

    @Test
    public void getGroupEntitiesTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        assertEquals(1, group.getEntities().length );
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
        PrimaryEntityIndex<String> index = new PrimaryEntityIndex<String>(group, (e, c) -> "positionEntities");
        pool.addEntityIndex("positions", index);
        index = (PrimaryEntityIndex<String>) pool.getEntityIndex("positions");
        assertNotNull(index);
        assertTrue(index.hasEntity("positionEntities"));

    }

    @Test(expected = PoolEntityIndexDoesAlreadyExistException.class)
    public void duplicateEntityIndexTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<String> index = new PrimaryEntityIndex<String>(group, (e, c) -> "positionEntities");
        pool.addEntityIndex("duplicate", index);
        pool.addEntityIndex("duplicate", index);

    }


    @Test(expected = PoolEntityIndexDoesNotExistException.class)
    public void deactivateAndRemoveEntityIndicesTest() {
        entity.addComponent(TestComponentIds.Position, new Position());
        Group group = pool.getGroup(TestMatcher.Position());
        PrimaryEntityIndex<String> index = new PrimaryEntityIndex<String>(group, (e, c) -> "positionEntities");
        pool.addEntityIndex("positions", index);

        pool.deactivateAndRemoveEntityIndices();
        index = (PrimaryEntityIndex<String>) pool.getEntityIndex("positions");


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
        pool.OnEntityCreated = (pool, entity)-> {};
        assertNotNull(pool.OnEntityCreated);
        pool.reset();
        assertNull(pool.OnEntityCreated);

    }

    @Test
    public void updateGroupsComponentAddedOrRemovedTest() {
        Position position = new Position();
        Group group = pool.getGroup(TestMatcher.Position());
        group.OnEntityAdded = ( g, e, idx, pc)-> assertEquals(TestComponentIds.Position,idx);

        entity.addComponent(TestComponentIds.Position, position);
        pool.updateGroupsComponentAddedOrRemoved(entity, TestComponentIds.Position, position);
        pool.updateGroupsComponentAddedOrRemoved(entity, TestComponentIds.Position, position);
        //pool.OnGroupCleared = (pool, group)->  assertNull(pool.OnEntityCreated);

    }

    @Test
    public void updateGroupsComponentReplacedTest() {
        Position position = new Position();
        Position position2 = new Position();
        Group group = pool.getGroup(TestMatcher.Position());
        group.OnEntityUpdated = ( g, e, idx, pc, nc)-> {
                System.out.println("OnEntityRemoved...........");
                assertEquals(position2,nc);
        };

        entity.addComponent(TestComponentIds.Position, position);
        pool.updateGroupsComponentReplaced(entity, TestComponentIds.Position, position, position2);

    }

    @Test
    public void createEntityCollectorTest() {
        BasePool[]  pools =  new BasePool[] {pool};
        EntityCollector collector = BasePool.createEntityCollector(pools, TestMatcher.Position());
        assertNotNull(collector);

    }



}

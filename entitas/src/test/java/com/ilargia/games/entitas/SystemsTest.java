package com.ilargia.games.entitas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.View;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.interfaces.*;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.Stack;

import static org.junit.Assert.*;

public class SystemsTest {

    private BasePool pool;
    private Systems systems;
    private MoveSystem moveSystem;

    public class MoveSystem implements IExecuteSystem, IInitializeSystem, ICleanupSystem, ITearDownSystem, ISetPool<BasePool>, ISetPools<Object> {
        public Group<Entity> _group;
        public boolean flagExecute = false;
        public boolean flagInitialize = false;
        public boolean flagCleanup = false;
        public boolean flagTearDown = false;
        public Object pools;

        @Override
        public void setPool(BasePool pool) {
            _group = pool.getGroup(Matcher.AllOf(TestMatcher.View()));
        }

        @Override
        public void setPools(Object pools) {
            this.pools = pools;
        }

        @Override
        public void execute() {
            flagExecute = true;
        }


        @Override
        public void initialize() {
            flagInitialize = true;
        }

        @Override
        public void cleanup() {
            flagCleanup = true;
        }

        @Override
        public void tearDown() {
            flagTearDown = true;
        }

    }

    private class TestReactive implements IReactiveSystem, IEntityCollectorSystem{
        public boolean flagExecute = false;

        @Override
        public TriggerOnEvent getTrigger() {
            return ((Matcher)Matcher.AllOf(TestMatcher.Position())).OnEntityAdded();
        }

        @Override
        public void execute(Array entities) {
            flagExecute = true;
        }

        @Override
        public EntityCollector getEntityCollector() {
            return new EntityCollector(pool.getGroup(Matcher.AllOf(TestMatcher.Position())), GroupEventType.OnEntityAdded);
        }
    }


    @Before
    public void setUp() throws Exception {
       systems = new Systems();
        pool = PoolTest.createTestPool();
        moveSystem = new MoveSystem();
        moveSystem.flagExecute = false;
        moveSystem.flagInitialize = false;
        moveSystem.flagCleanup = false;
        moveSystem.flagTearDown = false;

    }

    @Test
    public void addSystemTest() {
        systems.addSystem(pool, moveSystem);
        assertNotNull(moveSystem._group);

    }


    @Test
    public void addSystem2Test() {
        Object poolsTest = new Object();
        systems.addSystem(pool, moveSystem, poolsTest);
        assertEquals(poolsTest, moveSystem.pools);

    }

    //@Test
    public void addReactiveSystemTest() {
        TestReactive reactiveSystem = new TestReactive();
        systems.addSystem(pool, reactiveSystem);

        pool.createEntity().
                addComponent(TestComponentIds.Position, new Position(100, 100));

        assertTrue(reactiveSystem.flagExecute);

    }

    @Test
    public void systemMethodsTest() {
        systems.addSystem(pool, moveSystem);
        systems.initialize();
        systems.execute();
        systems.cleanup();
        systems.tearDown();

        assertTrue(moveSystem.flagExecute);
        assertTrue(moveSystem.flagInitialize);
        assertTrue(moveSystem.flagCleanup);
        assertTrue(moveSystem.flagTearDown);

    }




}

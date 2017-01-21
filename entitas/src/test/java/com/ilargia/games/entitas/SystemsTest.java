package com.ilargia.games.entitas;

import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.entitas.interfaces.*;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;
import com.ilargia.games.entitas.utils.TestComponentIds;
import com.ilargia.games.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SystemsTest {

    private BaseContext pool;
    private Systems systems;
    private MoveSystem moveSystem;

    private EventBus<Entity> bus;


    public FactoryEntity<Entity> factoryEntity() {
        return (int totalComponents, Stack<IComponent>[] componentPools,
                ContextInfo contextInfo) -> {
            return new Entity(totalComponents, componentPools, contextInfo, bus);
        };
    }

    public BaseContext createTestPool() {
        return new BaseContext(TestComponentIds.totalComponents, 0,
                new ContextInfo("Test", TestComponentIds.componentNames(),
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
        systems = new Systems();
        pool = createTestPool();
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

    @Test
    public void addReactiveSystemTest() {
        TestReactive reactiveSystem = new TestReactive();

        systems.addSystem(pool, reactiveSystem);
        systems.activateReactiveSystems();
        pool.createEntity().
                addComponent(TestComponentIds.Position, new Position(100, 100));

        systems.execute(1);

        assertTrue(reactiveSystem.flagExecute);

    }

    @Test
    public void addReactiveSystem2Test() {
        TestReactive reactiveSystem = new TestReactive();
        Object poolsTest = new Object();

        systems.addSystem(pool, reactiveSystem, poolsTest);
        systems.deactivateReactiveSystems();
        pool.createEntity().
                addComponent(TestComponentIds.Position, new Position(100, 100));

        systems.execute(1);


        assertTrue(reactiveSystem.flagExecute);

    }

    @Test
    public void systemMethodsTest() {
        systems.addSystem(pool, moveSystem);
        systems.initialize();
        systems.execute(1);
        systems.cleanup();
        systems.tearDown();

        assertTrue(moveSystem.flagExecute);
        assertTrue(moveSystem.flagInitialize);
        assertTrue(moveSystem.flagCleanup);
        assertTrue(moveSystem.flagTearDown);

    }

    public class MoveSystem implements ISystem.IExecuteSystem, ISystem.IInitializeSystem, ISystem.ICleanupSystem, ISystem.ITearDownSystem, ISetPool<BaseContext>, ISetPools<Object> {
        public Group<Entity> _group;
        public boolean flagExecute = false;
        public boolean flagInitialize = false;
        public boolean flagCleanup = false;
        public boolean flagTearDown = false;
        public Object pools;

        @Override
        public void setPool(BaseContext pool) {
            _group = pool.getGroup(Matcher.AllOf(TestMatcher.View()));
        }

        @Override
        public void setPools(Object pools) {
            this.pools = pools;
        }

        @Override
        public void execute(float deltaTime) {
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

    public class TestReactive implements IReactiveSystem, IEntityCollectorSystem {
        public boolean flagExecute = false;

        @Override
        public TriggerOnEvent getTrigger() {
            return TestMatcher.Position().OnEntityAdded();
        }

        @Override
        public void execute(List entities) {
            flagExecute = true;
        }

        @Override
        public Collector getEntityCollector() {
            return new Collector(pool.getGroup(Matcher.AllOf(TestMatcher.Position())), GroupEvent.Added);
        }
    }


}

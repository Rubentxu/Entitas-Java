package com.ilargia.games.entitas;

import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.FactoryEntity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.ITearDownSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.factories.CollectionsFactory;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.entitas.systems.Systems;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SystemsTest {

    private Context context;
    private Systems systems;
    private MoveSystem moveSystem;


    public FactoryEntity<Entity> factoryEntity() {
        return (int totalComponents, Stack<IComponent>[] componentPools,
                ContextInfo contextInfo) -> {
            return new Entity(totalComponents, componentPools, contextInfo);
        };
    }

    public Context createTestPool() {
        return new Context(TestComponentIds.totalComponents, 0,
                new ContextInfo("Test", TestComponentIds.componentNames(),
                        TestComponentIds.componentTypes()), factoryEntity());
    }

    private void createCollections() {
        new Collections(new CollectionsFactory() {
            @Override
            public <T> List createList(Class<T> clazz) {
                return new ArrayList();
            }

            @Override
            public <T> Set createSet(Class<T> clazz) {
                return new HashSet();
            }

            @Override
            public <K, V> Map createMap(Class<K> keyClazz, Class<V> valueClazz) {
                return new HashMap();
            }

        });
    }

    @Before
    public void setUp() throws Exception {
        createCollections();
        systems = new Systems();
        context = createTestPool();
        moveSystem = new MoveSystem(context);
        moveSystem.flagExecute = false;
        moveSystem.flagInitialize = false;
        moveSystem.flagCleanup = false;
        moveSystem.flagTearDown = false;

    }

    @Test
    public void addTest() {
        systems.add(moveSystem);
        assertNotNull(moveSystem._group);

    }

    @Test
    public void add2Test() {
        Object contextsTest = new Object();
        systems.add(moveSystem);
        // assertEquals(contextsTest, moveSystem.contexts);

    }

    @Test
    public void addReactiveSystemTest() {
        TestReactive reactiveSystem = new TestReactive(context);

        systems.add(reactiveSystem);
        systems.activateReactiveSystems();
        context.createEntity().
                addComponent(TestComponentIds.Position, new Position(100, 100));

        systems.execute(1);

        // assertTrue(reactiveSystem.flagExecute);

    }

    @Test
    public void addReactiveSystem2Test() {
        TestReactive reactiveSystem = new TestReactive(context);
        Object contextsTest = new Object();

        systems.add(reactiveSystem);
        systems.deactivateReactiveSystems();
        context.createEntity().
                addComponent(TestComponentIds.Position, new Position(100, 100));

        systems.execute(1);

        context.createEntity().
                addComponent(TestComponentIds.Position, new Position(100, 100));

        systems.execute(1);


        // assertTrue(reactiveSystem.flagExecute);

    }

    @Test
    public void systemMethodsTest() {
        systems.add(moveSystem);
        systems.initialize();
        systems.execute(1);
        systems.cleanup();
        systems.tearDown();

        assertTrue(moveSystem.flagExecute);
        assertTrue(moveSystem.flagInitialize);
        assertTrue(moveSystem.flagCleanup);
        assertTrue(moveSystem.flagTearDown);

    }

    public class MoveSystem implements IExecuteSystem, IInitializeSystem, ICleanupSystem, ITearDownSystem {
        public Group<TestEntity> _group;
        public boolean flagExecute = false;
        public boolean flagInitialize = false;
        public boolean flagCleanup = false;
        public boolean flagTearDown = false;

        public MoveSystem(Context<TestEntity> context) {
            _group = context.getGroup(Matcher.AllOf(TestMatcher.View()));

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

    public class TestReactive extends ReactiveSystem<TestEntity> {
        public boolean flagExecute = false;

        protected TestReactive(IContext<TestEntity> context) {
            super(context);
        }

        @Override
        public void execute(List entities) {
            flagExecute = true;
        }

        @Override
        public void execute(float deltaTime) {

        }

        @Override
        protected Collector<TestEntity> getTrigger(IContext<TestEntity> context) {
            return new Collector(context.getGroup(Matcher.AllOf(TestMatcher.Position())), GroupEvent.Added);
        }

        @Override
        protected boolean filter(TestEntity entity) {
            return true;
        }

        @Override
        public void activate() {

        }

        @Override
        public void deactivate() {

        }

        @Override
        public void clear() {

        }
    }


}

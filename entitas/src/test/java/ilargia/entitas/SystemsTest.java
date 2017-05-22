package ilargia.entitas;

import ilargia.entitas.api.ContextInfo;
import ilargia.entitas.api.entitas.EntityBaseFactory;
import ilargia.entitas.api.IContext;
import ilargia.entitas.api.system.ICleanupSystem;
import ilargia.entitas.api.system.IExecuteSystem;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.api.system.ITearDownSystem;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.components.Position;
import ilargia.entitas.factories.CollectionsFactories;
import ilargia.entitas.group.GroupEvent;
import ilargia.entitas.factories.EntitasCollections;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;
import ilargia.entitas.systems.ReactiveSystem;
import ilargia.entitas.systems.Systems;
import ilargia.entitas.utils.TestComponentIds;
import ilargia.entitas.utils.TestEntity;
import ilargia.entitas.utils.TestMatcher;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SystemsTest {

    private Context context;
    private Systems systems;
    private MoveSystem moveSystem;


    public EntityBaseFactory<Entity> factoryEntity() {
        return () -> {
            return new Entity();
        };
    }

    public Context createTestPool() {
        return new Context(TestComponentIds.totalComponents, 0,
                new ContextInfo("Test", TestComponentIds.componentNames(),
                        TestComponentIds.componentTypes()), factoryEntity(), null);
    }

    private void createCollections() {
        new EntitasCollections(new CollectionsFactories(){});
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

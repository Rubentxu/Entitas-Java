package com.ilargia.games.core.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.api.GUIFactory;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.factories.CollectionFactories;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.states.game.component.scene.GameWorld;
import com.ilargia.games.states.game.gen.Entitas;
import com.ilargia.games.core.gen.game.GameEntity;
import com.ilargia.games.core.gen.game.GameMatcher;
import com.ilargia.games.core.gen.scene.SceneContext;

import java.util.List;
import java.util.Map;


public class SceneSystem extends ReactiveSystem<GameEntity> implements IInitializeSystem, ICleanupSystem {

    public static int BOX2D_VELOCITY_ITERATIONS = 6;
    public static int BOX2D_POSITION_ITERATIONS = 10;
    private final SceneContext context;
    private final Group<GameEntity> gameElements;
    private final Map<String, GUIFactory> factories;
    private World physics;
    //private Map<String, GameElementFactory> entityFactories;


    public SceneSystem(Entitas entitas, World world) {
        super(entitas.game);
        this.physics = world;
        this.context = entitas.scene;
        this.gameElements = entitas.game.getGroup(Matcher.AllOf(GameMatcher.Element(), GameMatcher.RigidBody()));
        this.factories = CollectionFactories.createMap(String.class, GUIFactory.class);
    }

    @Override
    protected Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return context.createCollector(GameMatcher.Element());
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasElement() && (!entity.hasTextureView() || !entity.hasRigidBody());
    }


    @Override
    public void initialize() {
        GameWorld world = context.getGameWorld();
        OrthographicCamera camera = context.getCamera().camera;
        camera.position.set(world.width / 2, world.height / 2, 0);
        camera.viewportWidth = world.width;
        camera.viewportHeight = world.height;

    }

    @Override
    protected void execute(List<GameEntity> gameEntities) {
        for (GameEntity gameEntity : gameEntities) {
            factories.get(gameEntity.getElement().type).create(gameEntity);
        }
    }

    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), BOX2D_VELOCITY_ITERATIONS, BOX2D_POSITION_ITERATIONS);
    }


}

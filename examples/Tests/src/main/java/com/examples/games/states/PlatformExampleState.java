package com.examples.games.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.examples.games.ExamplesEngine;
import com.examples.games.entities.Box;
import com.examples.games.entities.Ground;
import com.examples.games.entities.Mariano;
import com.examples.games.scenes.PlatformExamples;
import ilargia.egdx.impl.GameStateGDX;
import ilargia.egdx.impl.managers.PhysicsManagerGDX;
import ilargia.egdx.impl.managers.SceneManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.egdx.logicbricks.system.actuator.ActuatorSystem;
import ilargia.egdx.logicbricks.system.actuator.CreateRadialGravityActuatorSystem;
import ilargia.egdx.logicbricks.system.actuator.RadialGravityActuatorSystem;
import ilargia.egdx.logicbricks.system.game.AddInputControllerSystem;
import ilargia.egdx.logicbricks.system.game.AnimationSystem;
import ilargia.egdx.logicbricks.system.game.RigidBodySystem;
import ilargia.egdx.logicbricks.system.render.BackgroundRenderSystem;
import ilargia.egdx.logicbricks.system.render.DebugRendererSystem;
import ilargia.egdx.logicbricks.system.render.LigthRendererSystem;
import ilargia.egdx.logicbricks.system.render.TextureRendererSystem;
import ilargia.egdx.logicbricks.system.scene.SceneSystem;
import ilargia.egdx.logicbricks.system.sensor.*;


public class PlatformExampleState extends GameStateGDX {
    private final ExamplesEngine engine;
    private final Entitas entitas;
    private SceneManagerGDX sceneManager;

    public PlatformExampleState(ExamplesEngine engine, Entitas entitas) {
        this.engine = engine;
        this.entitas = entitas;

    }

    @Override
    public void loadResources() {
        sceneManager = engine.getManager(SceneManagerGDX.class);
        sceneManager.addEntityFactory("Ground", new Ground());
        sceneManager.addEntityFactory("Mariano", new Mariano());
        sceneManager.addEntityFactory("Box", new Box());
        sceneManager.addSceneFactory("Pruebas", new PlatformExamples());
        sceneManager.initialize();
    }

    @Override
    public void initialize() {
        Indexed.initialize(entitas);
        entitas.scene.setCamera((OrthographicCamera) engine.getManager(SceneManagerGDX.class).getDefaultCamera());

        systems.add(new CollisionSensorSystem(entitas, engine))
                .add(new CreateNearSensorSystem(entitas, engine))
                .add(new CreateRadarSensorSystem(entitas, engine))
                .add(new DelaySensorSystem(entitas))
                .add(new NearSensorSystem(entitas, engine))
                .add(new RadarSensorSystem(entitas, engine))
                .add(new RaySensorSystem(entitas, engine.getManager(PhysicsManagerGDX.class).getPhysics()))
                .add(new AddInputControllerSystem(entitas, engine))
                .add(new SceneSystem(entitas, engine))
                .add(new RigidBodySystem(entitas))
                .add(new AnimationSystem(entitas))
                .add(new ActuatorSystem(entitas))
                .add(new CreateRadialGravityActuatorSystem(entitas, engine))
                .add(new RadialGravityActuatorSystem(entitas))
                .add(new LigthRendererSystem(entitas, engine))
                .add(new BackgroundRenderSystem(entitas, engine))
                .add(new TextureRendererSystem(entitas, engine))
                .add(new DebugRendererSystem(entitas, engine.getManager(PhysicsManagerGDX.class).getPhysics(),
                        engine.getManager(SceneManagerGDX.class).getBatch()));

        sceneManager.createScene("Pruebas");
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void unloadResources() {
        //context.core.destroyAllEntities();
        systems.clearSystems();
    }

}

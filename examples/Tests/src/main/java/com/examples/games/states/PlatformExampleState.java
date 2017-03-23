package com.examples.games.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.examples.games.ExamplesEngine;
import com.examples.games.entities.Ground;
import com.examples.games.entities.Mariano;
import com.examples.games.scenes.PlatformExamples;
import com.ilargia.games.egdx.impl.GameStateGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.logicbricks.system.actuator.CameraActuatorSystem;
import com.ilargia.games.egdx.logicbricks.system.game.*;
import com.ilargia.games.egdx.logicbricks.system.render.*;
import com.ilargia.games.egdx.logicbricks.system.scene.SceneSystem;
import com.ilargia.games.egdx.logicbricks.system.sensor.*;


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
                .add(new NearSensorSystem(entitas))
                .add(new RadarSensorSystem(entitas))
                .add(new RaySensorSystem(entitas, engine.getManager(PhysicsManagerGDX.class).getPhysics()))
                .add(new AddInputControllerSystem(entitas, engine))
                .add(new InputControllerSystem(engine))
                .add(new SceneSystem(entitas, engine))
                .add(new RigidBodySystem(entitas))
                .add(new AnimationSystem(entitas))
                .add(new CameraActuatorSystem(entitas))
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

package com.examples.games.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.examples.games.ExamplesEngine;
import com.examples.games.entities.Ground;
import com.examples.games.scenes.SceneManagerExamples;
import com.ilargia.games.egdx.api.managers.SceneManager;
import com.ilargia.games.egdx.impl.GameStateGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.system.render.DebugRendererSystem;
import com.ilargia.games.egdx.logicbricks.system.render.TextureRendererSystem;
import com.ilargia.games.egdx.logicbricks.system.scene.SceneSystem;
import com.ilargia.games.egdx.logicbricks.system.sensor.*;


public class PlatformExampleState extends GameStateGDX {
    private final ExamplesEngine engine;
    private final Entitas entitas;
    private SceneManagerExamples sceneManager;

    public PlatformExampleState(ExamplesEngine engine, Entitas entitas) {
        this.engine = engine;
        this.entitas = entitas;

    }

    @Override
    public void loadResources() {
        sceneManager = engine.getManager(SceneManagerExamples.class);
        sceneManager.addEntityFactory("Ground", new Ground());
        //sceneManager.addEntityFactory("Mariano", new Mariano());
    }

    @Override
    public void initialize() {
        entitas.scene.setCamera((OrthographicCamera) engine.getManager(SceneManagerExamples.class).getDefaultCamera());

        systems.add(new CollisionSensorSystem(entitas))
                .add(new CreateNearSensorSystem(entitas, engine))
                .add(new CreateRadarSensorSystem(entitas, engine))
                .add(new DelaySensorSystem(entitas))
                .add(new IndexingLinkSensorSystem(entitas))
                .add(new NearSensorSystem(entitas))
                .add(new RadarSensorSystem(entitas))
                .add(new RaySensorSystem(entitas, engine.getManager(PhysicsManagerGDX.class).getPhysics()))
                .add(new NearSensorSystem(entitas))
                .add(new SceneSystem(engine, entitas))
                .add(new TextureRendererSystem(entitas,engine.getManager(SceneManagerExamples.class).getBatch()))
                .add(new DebugRendererSystem(entitas,engine.getManager(PhysicsManagerGDX.class).getPhysics()));

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

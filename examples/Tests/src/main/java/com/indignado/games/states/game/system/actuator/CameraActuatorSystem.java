package com.indignado.games.states.game.system.actuator;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.indignado.games.states.game.component.actuator.CameraActuator;
import com.indignado.games.states.game.component.game.RigidBody;
import com.indignado.games.states.game.gen.*;
import com.indignado.games.states.game.gen.actuator.ActuatorEntity;
import com.indignado.games.states.game.gen.actuator.ActuatorMatcher;
import com.indignado.games.states.game.gen.game.GameContext;
import com.indignado.games.states.game.gen.game.GameEntity;
import com.indignado.games.states.game.gen.scene.SceneContext;

import java.util.List;


public class CameraActuatorSystem extends ReactiveSystem<ActuatorEntity> implements IInitializeSystem {
    private final GameContext gameContext;
    private final SceneContext sceneContext;
    private Camera camera;
    private GameEntity followEntity;


    public CameraActuatorSystem(Entitas entitas, World world) {
        super(entitas.actuator);
        this.gameContext = entitas.game;
        this.sceneContext = entitas.scene;

    }

    @Override
    public void initialize() {
        camera = sceneContext.getCamera().camera;
        followEntity = gameContext.getPlayerEntity();
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(ActuatorMatcher.CameraActuator());
    }

    @Override
    protected boolean filter(ActuatorEntity entity) {
        return entity.hasCameraActuator();

    }

    @Override
    protected void execute(List<ActuatorEntity> entities) {
        for (ActuatorEntity e : entities) {
            CameraActuator actuator = e.getCameraActuator();

            if (followEntity != null) {
                RigidBody rc = followEntity.getRigidBody();
                Transform transform = rc.body.getTransform();
                Vector3 position = camera.position;
                position.x += (transform.getPosition().x - position.x) * actuator.damping;
                position.y += (transform.getPosition().y - position.y) * actuator.damping;
            }
        }

    }

}

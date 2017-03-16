package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.logicbricks.component.actuator.CameraActuator;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameContext;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;

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

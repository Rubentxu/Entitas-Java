package com.ilargia.games.egdx.logicbricks.system.actuator;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Transform;
import com.ilargia.games.egdx.logicbricks.component.actuator.CameraActuator;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorEntity;
import com.ilargia.games.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.group.Group;


public class CameraActuatorSystem implements IInitializeSystem, IExecuteSystem{
    private final SceneContext sceneContext;
    private final Group<ActuatorEntity> group;
    private Camera camera;

    public CameraActuatorSystem(Entitas entitas) {
        this.sceneContext = entitas.scene;
        this.group = entitas.actuator.getGroup(ActuatorMatcher.CameraActuator());

    }

    @Override
    public void initialize() {
        camera = sceneContext.getCamera().camera;
    }

    @Override
    public void execute(float time) {
        for (ActuatorEntity e : group.getEntities()) {
            CameraActuator actuator = e.getCameraActuator();
            GameEntity followEntity = Indexed.getTagEntity(actuator.followTagEntity);
            if (followEntity != null) {
                RigidBody rc = followEntity.getRigidBody();
                Transform transform = rc.body.getTransform();
                Vector3 position = camera.position;
                position.x += (transform.getPosition().x + actuator.minDistance.x - position.x) * actuator.damping;
                position.y += (transform.getPosition().y + actuator.minDistance.y - position.y) * actuator.height;
            }
        }

    }

}

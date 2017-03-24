package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Transform;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.data.interfaces.Actuator;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Actuator"})
public class CameraActuator implements IComponent {
    public Actuator actuator;

    public CameraActuator(Camera camera, short height, float damping, float minDistanceX, float minDistanceY, String followTagEntity) {
        this.actuator = (indexOwner) -> {
            GameEntity followEntity = Indexed.getTagEntity(followTagEntity);
            if (followEntity != null) {
                RigidBody rc = followEntity.getRigidBody();
                Transform transform = rc.body.getTransform();
                Vector3 position = camera.position;
                position.x += (transform.getPosition().x + minDistanceX - position.x) * damping;
                position.y += (transform.getPosition().y + minDistanceY - position.y) * height;
            }
        };

    }
}

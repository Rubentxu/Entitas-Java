package ilargia.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Transform;
import ilargia.egdx.logicbricks.component.game.RigidBody;
import ilargia.egdx.logicbricks.data.interfaces.Actuator;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

import java.util.Set;


@Component(pools = {"Actuator"})
public class CameraActuator implements IComponent {
    public Actuator actuator;

    public CameraActuator(Camera camera, short height, float damping, float minDistanceX, float minDistanceY, String followTagEntity) {
        this.actuator = (indexOwner) -> {
            Set<GameEntity> followEntities = Indexed.getTagEntities(followTagEntity);
            for (GameEntity followEntity : followEntities) {
                RigidBody rc = followEntity.getRigidBody();
                Transform transform = rc.body.getTransform();
                Vector3 position = camera.position;
                position.x += (transform.getPosition().x + minDistanceX - position.x) * damping;
                position.y += (transform.getPosition().y + minDistanceY - position.y) * height;
            }
        };

    }
}

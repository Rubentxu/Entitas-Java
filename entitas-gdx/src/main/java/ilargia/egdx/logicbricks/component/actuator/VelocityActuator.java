package ilargia.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.math.Vector2;
import ilargia.egdx.logicbricks.component.game.RigidBody;
import ilargia.egdx.logicbricks.data.interfaces.Actuator;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class VelocityActuator implements IComponent {
    public Actuator actuator;

    public VelocityActuator(Vector2 velocity, float angularVelocity) {
        this.actuator = (indexOwner)-> {
            GameEntity owner = Indexed.getInteractiveEntity(indexOwner);
            RigidBody rigidBody = owner.getRigidBody();
            rigidBody.body.setLinearVelocity(velocity);
            rigidBody.body.setAngularVelocity(angularVelocity);
        };
    }
}

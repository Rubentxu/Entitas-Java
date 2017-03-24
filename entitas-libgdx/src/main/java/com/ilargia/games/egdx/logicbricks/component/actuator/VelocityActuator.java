package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.component.game.TextureView;
import com.ilargia.games.egdx.logicbricks.data.interfaces.Actuator;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

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

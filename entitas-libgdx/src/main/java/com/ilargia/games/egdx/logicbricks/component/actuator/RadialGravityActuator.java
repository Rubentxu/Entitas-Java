package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Transform;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.data.interfaces.Actuator;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class RadialGravityActuator implements IComponent {
    public Actuator actuator;
    public float gravity = -9.8f;
    public float radius = 0;
    public float gravityFactor = 4;

    public RadialGravityActuator(SensorContext context, float gravity, float radius, float gravityFactor) {

        this.actuator = (indexOwner)-> {
            GameEntity owner = Indexed.getInteractiveEntity(indexOwner);
            context.createEntity().addRadarSensor("",)
            RigidBody rc = owner.getRigidBody();
            Transform transform = rc.body.getTransform();
            effect.setPosition(transform.getPosition().x + locaPosX, transform.getPosition().y + locaPosY);
            effect.update(Gdx.graphics.getDeltaTime());
            if(autoStart && effect.isComplete()) effect.start();

        };
    }


}

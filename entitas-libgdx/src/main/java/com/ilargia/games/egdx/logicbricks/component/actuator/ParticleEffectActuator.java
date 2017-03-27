package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Transform;
import com.ilargia.games.egdx.logicbricks.data.RigidBody;
import com.ilargia.games.egdx.logicbricks.data.interfaces.Actuator;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class ParticleEffectActuator implements IComponent {
    public Actuator actuator;
    public ParticleEffect particleEffect;

    public ParticleEffectActuator(ParticleEffect effect, boolean autoStart, float locaPosX, float locaPosY) {
        this.particleEffect = effect;
        this.actuator = (indexOwner)-> {
            GameEntity owner = Indexed.getInteractiveEntity(indexOwner);
            RigidBody rc = owner.getRigidBody();
            Transform transform = rc.body.getTransform();
            effect.setPosition(transform.getPosition().x + locaPosX, transform.getPosition().y + locaPosY);
            effect.update(Gdx.graphics.getDeltaTime());
            if(autoStart && effect.isComplete()) effect.start();

        };
    }


}

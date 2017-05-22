package ilargia.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.physics.box2d.Transform;
import ilargia.egdx.logicbricks.component.game.RigidBody;
import ilargia.egdx.logicbricks.data.interfaces.Actuator;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

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

package ilargia.egdx.logicbricks.component.game;

import com.badlogic.gdx.physics.box2d.Body;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Game"})
public class RigidBody implements IComponent {
    public Body body;
}

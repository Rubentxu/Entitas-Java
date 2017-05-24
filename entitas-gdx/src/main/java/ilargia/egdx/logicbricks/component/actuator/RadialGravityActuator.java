package ilargia.egdx.logicbricks.component.actuator;


import ilargia.egdx.logicbricks.data.interfaces.Actuator;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class RadialGravityActuator implements IComponent {
    public Actuator actuator;
    public float gravity;
    public float radius;
    public float gravityFactor;

    public RadialGravityActuator(float gravity, float radius, float gravityFactor) {
        this.gravity = gravity;
        this.radius = radius;
        this.gravityFactor = gravityFactor;

    }


}

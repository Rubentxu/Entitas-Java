package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class CollisionSensor implements IComponent {
    // Config Values
    public String targetTag;

    public boolean collisionSignal;


    public CollisionSensor(String targetTag) {
        this.targetTag = targetTag;
        this.collisionSignal = false;
    }
}


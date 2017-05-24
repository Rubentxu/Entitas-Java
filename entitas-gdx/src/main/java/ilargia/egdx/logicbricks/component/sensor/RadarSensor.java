package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;
import ilargia.egdx.logicbricks.data.Axis2D;

@Component(pools = {"Sensor"})
public class RadarSensor implements IComponent {

    public String targetTag;
    public Axis2D axis2D;
    public float angle = 0;
    public float distance = 0;

    public boolean collisionSignal;

    public RadarSensor(String targetTag, Axis2D axis2D, float distance, float angle) {
        this.targetTag = targetTag;
        this.axis2D = axis2D;
        this.distance = distance;
        this.angle = angle;
        this.collisionSignal = false;

    }

}


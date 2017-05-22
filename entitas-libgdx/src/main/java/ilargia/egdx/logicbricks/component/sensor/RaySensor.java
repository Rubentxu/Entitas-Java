package ilargia.egdx.logicbricks.component.sensor;


import ilargia.egdx.logicbricks.data.Axis2D;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Set;

@Component(pools = {"Sensor"})
public class RaySensor implements IComponent {

    public String targetTag;
    public Axis2D axis2D;
    public float range = 0.0f;
    public boolean xRayMode = false;

    public boolean collisionSignal;
    public Set<Integer> rayContactList;

    public RaySensor(String targetTag, Axis2D axis2D, float range, boolean xRayMode) {
        this.targetTag = targetTag;
        this.axis2D = axis2D;
        this.range = range;
        this.xRayMode = xRayMode;
        this.collisionSignal = false;
        this.rayContactList = EntitasCollections.createSet(Integer.class);

    }

}


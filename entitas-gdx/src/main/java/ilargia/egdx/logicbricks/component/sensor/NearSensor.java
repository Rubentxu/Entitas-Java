package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Set;

@Component(pools = {"Sensor"})
public class NearSensor implements IComponent {
    // Config Values
    public String targetTag;
    public float distance;
    public float resetDistance;

    public Set<Integer> distanceContactList;
    public Set<Integer> resetDistanceContactList;
    public boolean initContact = false;


    public NearSensor(String targetTag, float distance, float resetDistance) {
        this.targetTag = targetTag;
        this.distance = distance;
        this.resetDistance = resetDistance;
        this.distanceContactList = EntitasCollections.createSet(Integer.class);
        this.resetDistanceContactList = EntitasCollections.createSet(Integer.class);
    }
}


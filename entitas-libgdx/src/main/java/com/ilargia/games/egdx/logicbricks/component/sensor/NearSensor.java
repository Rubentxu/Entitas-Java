package com.ilargia.games.egdx.logicbricks.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;

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


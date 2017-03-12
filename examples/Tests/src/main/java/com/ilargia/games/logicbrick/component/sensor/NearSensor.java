package com.ilargia.games.logicbrick.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.logicbrick.gen.Entitas;

import java.util.Set;

@Component(pools = {"Sensor"})
public class NearSensor implements IComponent {
    // Config Values
    public String targetTag;
    public float distance = 0;
    public float resetDistance = 0;

    public Set<Integer> distanceContactList;
    public Set<Integer> resetDistanceContactList;
    public boolean initContact = false;


    public NearSensor(String targetTag) {
        this.targetTag = targetTag;
        this.distance = 0;
        this.resetDistance = 0;
        this.distanceContactList = EntitasCollections.createSet(Integer.class);
        this.resetDistanceContactList = EntitasCollections.createSet(Integer.class);
    }
}


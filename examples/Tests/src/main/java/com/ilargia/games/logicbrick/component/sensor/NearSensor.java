package com.ilargia.games.logicbrick.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class NearSensor implements IComponent {
    // Config Values
    public String targetTag;
    public String targetPropertyName;
    public float distance = 0;
    public float resetDistance = 0;

    public boolean collisionSignal;


    public NearSensor(String targetTag) {
        this.targetTag = targetTag;
        this.collisionSignal = false;
    }
}


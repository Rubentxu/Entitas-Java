package com.ilargia.games.logicbrick.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.logicbrick.data.Axis2D;

@Component(pools = {"Sensor"})
public class RaySensor implements IComponent {

    public String targetTag;
    public Axis2D axis2D;
    public float range = 0.0f;
    public boolean xRayMode = false;

    public boolean collisionSignal;

    public RaySensor(String targetTag, Axis2D axis2D, float range, boolean xRayMode) {
        this.targetTag = targetTag;
        this.axis2D = axis2D;
        this.range = range;
        this.xRayMode = xRayMode;
        this.collisionSignal = false;

    }

}


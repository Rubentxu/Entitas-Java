package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class MouseOverSensor implements IComponent {
    public String targetTag;
    // Signal Values
    public Vector2 positionSignal;


    public MouseOverSensor(String targetTag) {
        this.targetTag = targetTag;
        this.positionSignal =  new Vector2();

    }

}


package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class MouseButtonSensor implements IComponent {  
    public int pointer;
    public boolean pressed;

    // Signal Values
    public Vector2 positionSignal;

    public MouseButtonSensor(int pointer, boolean pressed) {
        this.pointer = pointer;
        this.pressed = pressed;
        positionSignal =  new Vector2();

    }

}


package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class MouseButtonSensor implements IComponent {
    public enum MouseButton {
         RIGHT_DOWN, MIDDLE_DOWN, LEFT_DOWN, RIGHT_UP, MIDDLE_UP, LEFT_UP
    }

    public MouseButton mouseEvent;

    // Signal Values
    public MouseButton mouseEventSignal;
    public Vector2 positionSignal;

    public MouseButtonSensor(MouseButton event) {
        this.mouseEvent = event;
        this.mouseEventSignal = null;
        positionSignal =  new Vector2();

    }

}


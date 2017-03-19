package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class MouseSensor implements IComponent {
    public enum MouseEvent {
        MOUSE_OVER, MOVEMENT, WHEEL_DOWN, WHEEL_UP, RIGHT_BUTTON_DOWN,
        MIDDLE_BUTTON_DOWN, LEFT_BUTTON_DOWN, RIGHT_BUTTON_UP,
        MIDDLE_BUTTON_UP, LEFT_BUTTON_UP
    }

    public MouseEvent mouseEvent;
    public String targetTag;

    // Signal Values
    public MouseEvent mouseEventSignal;
    public Vector2 positionSignal = new Vector2();
    public int amountScrollSignal = 0;

    public MouseSensor(MouseEvent event, String targetTag) {
        this.mouseEvent = event;
        this.targetTag = targetTag;
        this.positionSignal =  new Vector2();
        this.amountScrollSignal = 0;
        this.mouseEventSignal = null;

    }

}


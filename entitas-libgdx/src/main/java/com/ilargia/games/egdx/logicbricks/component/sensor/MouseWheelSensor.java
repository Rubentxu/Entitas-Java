package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class MouseWheelSensor implements IComponent {
    public int amountScroll = 0;
    public boolean scrollSignal = false;


}


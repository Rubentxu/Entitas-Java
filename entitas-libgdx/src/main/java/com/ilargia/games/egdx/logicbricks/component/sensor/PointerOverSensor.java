package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class PointerOverSensor implements IComponent {
    public int pointer;
    public String targetTag;

    public PointerOverSensor(int pointer, String targetTag) {
        this.targetTag = targetTag;

    }

}


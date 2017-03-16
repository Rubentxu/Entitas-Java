package com.ilargia.games.egdx.logicbricks.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Frequency implements IComponent {
    public float tick;
    public float time;

    public Frequency(float tick) {
        this.tick = tick;
    }
}

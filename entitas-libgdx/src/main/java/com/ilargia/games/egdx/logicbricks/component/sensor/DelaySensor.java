package com.ilargia.games.egdx.logicbricks.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class DelaySensor implements IComponent {
    public float delay;
    public float duration;
    public boolean repeat;

    public float time;

    public DelaySensor(float delay, float duration, boolean repeat) {
        this.delay = delay;
        this.duration = duration;
        this.repeat = repeat;
        this.time = 0;
    }
}

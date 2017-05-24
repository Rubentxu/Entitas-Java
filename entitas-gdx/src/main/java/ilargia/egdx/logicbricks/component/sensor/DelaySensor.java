package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

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

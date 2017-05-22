package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Frequency implements IComponent {
    public float tick;
    public float time;

    public Frequency(float tick) {
        this.tick = tick;
    }
}

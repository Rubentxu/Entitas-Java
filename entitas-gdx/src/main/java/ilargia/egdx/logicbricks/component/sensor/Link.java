package ilargia.egdx.logicbricks.component.sensor;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Link implements IComponent {
    public String sensorReference;
    public int ownerEntity;
    public boolean isOpen;
    public boolean isChanged;
    public boolean pulse;

    public Link(String sensorReference, int ownerEntity) {
        this.sensorReference = sensorReference;
        this.ownerEntity = ownerEntity;
        this.isOpen = false;
        this.isChanged = false;
        this.pulse = false;

    }

}

package ilargia.egdx.logicbricks.component.actuator;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class Link implements IComponent {
    public String actuatorReference;
    public int ownerEntity;
    public boolean isOpen;

    public Link(String actuatorReference, int ownerEntity, boolean isOpen) {
        this.actuatorReference = actuatorReference;
        this.ownerEntity = ownerEntity;
        this.isOpen = isOpen;

    }

}

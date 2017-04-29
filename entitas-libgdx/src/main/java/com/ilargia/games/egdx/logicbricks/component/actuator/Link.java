package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

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

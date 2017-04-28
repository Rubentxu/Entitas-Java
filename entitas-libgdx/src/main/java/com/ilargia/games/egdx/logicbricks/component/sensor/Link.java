package com.ilargia.games.egdx.logicbricks.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Link implements IComponent {
    public int ownerEntity;
    public boolean isOpen;
    public boolean isChanged;
    public boolean pulse;

    public Link(int ownerEntity) {
        this.ownerEntity = ownerEntity;
        this.isOpen = false;
        this.isChanged = false;
        this.pulse = false;

    }

}

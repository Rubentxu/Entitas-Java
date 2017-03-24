package com.ilargia.games.egdx.logicbricks.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Link implements IComponent {
    public int ownerEntity;
    public boolean isOpen;
    public boolean isChanged;
    public boolean pulse;
    public String nameReference;

    public Link(int ownerEntity, String nameReference) {
        this.ownerEntity = ownerEntity;
        this.nameReference = nameReference;
        this.isOpen = false;
        this.isChanged = false;
        this.pulse = false;

    }

}

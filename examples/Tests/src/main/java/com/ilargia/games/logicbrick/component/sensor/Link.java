package com.ilargia.games.logicbrick.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Link implements IComponent {
    public int targetEntity;
    public boolean isOpen;
    public boolean isChanged;
    public boolean pulse;

    public Link(int targetEntity) {
        this.targetEntity = targetEntity;
        this.isOpen = false;
        this.isChanged = false;
        this.pulse = false;

    }

}

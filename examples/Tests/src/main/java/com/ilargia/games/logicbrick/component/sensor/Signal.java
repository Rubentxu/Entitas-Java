package com.ilargia.games.logicbrick.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Signal implements IComponent {
    public boolean isOpen;
    public boolean isChanged;
    public boolean pulse;


    public Signal() {
        this.isOpen = false;
        this.isChanged = false;
        this.pulse = false;
    }
}

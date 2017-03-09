package com.ilargia.games.logicbrick.component.actuator;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
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

package com.ilargia.games.egdx.logicbricks.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Name implements IComponent {
    public String nameReference;

    public Name(String nameReference) {
        this.nameReference = nameReference;

    }

}

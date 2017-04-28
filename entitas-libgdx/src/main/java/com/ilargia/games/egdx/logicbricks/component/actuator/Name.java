package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class Name implements IComponent {
    public String nameReference;

    public Name(String nameReference) {
        this.nameReference = nameReference;

    }

}

package com.ilargia.games.egdx.logicbricks.component.game;

import com.ilargia.games.egdx.logicbricks.component.sensor.CollisionSensor;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Set;

@Component(pools = {"Game"})
public class Tags implements IComponent {
    public Set<String> values;

    public Tags(String  ...values) {
        if (this.values == null) {
            this.values = EntitasCollections.createSet(String.class);
        } else {
            this.values.clear();
        }
        for (String value : values) {
            this.values.add(value);
        }
    }
}


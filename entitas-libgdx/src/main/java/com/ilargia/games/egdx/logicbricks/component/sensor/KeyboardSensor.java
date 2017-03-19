package com.ilargia.games.egdx.logicbricks.component.sensor;

import com.badlogic.gdx.Input;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Set;

@Component(pools = {"Sensor"})
public class KeyboardSensor implements IComponent {
    // Config Values
    public int keyCode;

    public Set<Integer> keysCodeSignal;

    public KeyboardSensor(int keyCode) {
        this.keyCode = keyCode;
        this.keysCodeSignal = EntitasCollections.createSet(Integer.class);

    }
}


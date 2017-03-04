package com.ilargia.games.core.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Frequency implements IComponent {
    public float frequency;

}

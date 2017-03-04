package com.ilargia.games.core.component.sensor;


import com.ilargia.games.core.data.Sensor;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Mode implements IComponent {
    public enum Type { Negative, Positive }
    public Type mode;

}

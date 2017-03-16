package com.ilargia.games.egdx.logicbricks.component.game;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.egdx.logicbricks.component.sensor.DelaySensor;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;

import java.util.List;

@Component(pools = {"Game"})
public class DelaySensorComponents implements IComponent {
    public List<SensorEntity> delaySensors;

    public DelaySensorComponents(SensorEntity sensor) {
        if (this.delaySensors == null) {
            this.delaySensors = EntitasCollections.createList(DelaySensor.class);
        } else {
            for (SensorEntity delaySensor : this.delaySensors) {
                delaySensor.release(this);
            }
            this.delaySensors.clear();
        }
        if(sensor.hasDelaySensor()){
            sensor.retain(this);
            this.delaySensors.add(sensor);
        }

    }
}

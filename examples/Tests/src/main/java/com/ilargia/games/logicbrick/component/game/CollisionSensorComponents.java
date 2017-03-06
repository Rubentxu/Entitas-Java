package com.ilargia.games.logicbrick.component.game;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;
import com.ilargia.games.logicbrick.component.sensor.CollisionSensor;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;

import java.util.List;

@Component(pools = {"Game"})
public class CollisionSensorComponents implements IComponent {
    public List<SensorEntity> collisionSensors;

    public CollisionSensorComponents(SensorEntity sensor) {
        if (this.collisionSensors == null) {
            this.collisionSensors = EntitasCollections.createList(CollisionSensor.class);
        } else {
            for (SensorEntity collisionSensor : this.collisionSensors) {
                collisionSensor.release(this);
            }
            this.collisionSensors.clear();
        }        
        if(sensor.hasCollisionSensor()){
            sensor.retain(this);
            this.collisionSensors.add(sensor);
        }
        
    }
}

package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.logicbricks.component.sensor.MouseWheelSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class MouseWheelSensorSystem extends InputSensorSystem {
    private final Group<SensorEntity> sensorGroup;

    public MouseWheelSensorSystem(Entitas entitas) {
        this.sensorGroup = entitas.sensor.getGroup(Matcher.AllOf(SensorMatcher.MouseWheelSensor(), SensorMatcher.Link()));

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        return sensorEntity.getMouseWheelSensor().scrollSignal;
    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
            sensorEntity.getMouseWheelSensor().scrollSignal = false;
        }
    }

    @Override
    public boolean scrolled(int amount) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            MouseWheelSensor sensor = sensorEntity.getMouseWheelSensor();
            sensor.scrollSignal = true;
            sensor.amountScroll = amount;
        }
        return false;

    }

}


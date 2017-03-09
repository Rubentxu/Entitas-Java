package com.ilargia.games.logicbrick.system.sensor;


import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;
import com.ilargia.games.logicbrick.component.sensor.DelaySensor;
import com.ilargia.games.logicbrick.gen.sensor.SensorContext;
import com.ilargia.games.logicbrick.gen.sensor.SensorEntity;
import com.ilargia.games.logicbrick.gen.sensor.SensorMatcher;

public class DelaySensorSystem extends SensorSystem implements IExecuteSystem {

    private Group<SensorEntity> sensorGroup;

    public DelaySensorSystem(SensorContext context) {
        sensorGroup = context.getGroup(Matcher.AllOf(SensorMatcher.DelaySensor(), SensorMatcher.Signal()));

    }

    @Override
    protected boolean query(SensorEntity delaySensor, float deltaTime) {
        boolean isActive = false;
        DelaySensor sensor = delaySensor.getDelaySensor();
        if (sensor.time != -1) sensor.time += deltaTime;

        if (sensor.time >= sensor.delay) {            if (sensor.time >= (sensor.delay + sensor.duration)) {
                if (sensor.repeat) {
                    sensor.time = 0;
                } else {
                    sensor.time = -1;
                }
            } else {
                isActive = true;
            }
        }
        return isActive;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }

    }

}


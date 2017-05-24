package ilargia.egdx.logicbricks.system.sensor;


import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;
import ilargia.egdx.logicbricks.component.sensor.DelaySensor;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;

public class DelaySensorSystem extends SensorSystem {

    private Group<SensorEntity> sensorGroup;

    public DelaySensorSystem(Entitas entitas) {
        sensorGroup = entitas.sensor.getGroup(Matcher.AllOf(SensorMatcher.DelaySensor(), SensorMatcher.Link()));

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


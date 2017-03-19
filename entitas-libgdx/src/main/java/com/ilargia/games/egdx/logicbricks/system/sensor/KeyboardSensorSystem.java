package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.Input;
import com.ilargia.games.egdx.logicbricks.component.sensor.KeyboardSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class KeyboardSensorSystem extends InputSensorSystem  {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;

    public KeyboardSensorSystem(Entitas entitas) {
        this.sensorContex = entitas.sensor;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.KeyboardSensor(), SensorMatcher.Link()));


    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        boolean isActive = false;
        KeyboardSensor sensor = sensorEntity.getKeyboardSensor();

        if (sensor.keyCode == Input.Keys.UNKNOWN || sensor.keysCodeSignal.contains(sensor.keyCode)) {
            isActive = true;
        }
        return isActive;

    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        for (SensorEntity entity : sensorGroup.getEntities()) {
            entity.getKeyboardSensor().keysCodeSignal.add(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (SensorEntity entity : sensorGroup.getEntities()) {
            entity.getKeyboardSensor().keysCodeSignal.remove(keycode);
        }
        return false;
    }

}


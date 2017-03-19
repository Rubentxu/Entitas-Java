package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.component.sensor.MouseButtonSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class MouseButtonSensorSystem extends InputSensorSystem {
    private final Group<SensorEntity> sensorGroup;
    private final Camera camera;
    private Vector3 worldCoordinates;

    public MouseButtonSensorSystem(Entitas entitas, EngineGDX engine) {
        this.sensorGroup = entitas.sensor.getGroup(Matcher.AllOf(SensorMatcher.MouseButtonSensor(), SensorMatcher.Link()));
        camera = engine.getCamera();
        worldCoordinates = new Vector3(0, 0, 0);

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        boolean isActive = false;
        MouseButtonSensor sensor = sensorEntity.getMouseButtonSensor();
        if (sensor.mouseEventSignal != null && sensor.mouseEvent.equals(sensor.mouseEventSignal)) {
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
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        switch (button) {
            case Input.Buttons.LEFT:
                changeSensors(MouseButtonSensor.MouseButton.LEFT_DOWN, worldCoordinates);
                break;
            case Input.Buttons.MIDDLE:
                changeSensors(MouseButtonSensor.MouseButton.MIDDLE_DOWN, worldCoordinates);
                break;
            case Input.Buttons.RIGHT:
                changeSensors(MouseButtonSensor.MouseButton.RIGHT_DOWN, worldCoordinates);
                break;

        }
        return false;

    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);

        switch (button) {
            case Input.Buttons.LEFT:
                changeSensors(MouseButtonSensor.MouseButton.LEFT_UP, worldCoordinates);
                break;
            case Input.Buttons.MIDDLE:
                changeSensors(MouseButtonSensor.MouseButton.MIDDLE_UP, worldCoordinates);
                break;
            case Input.Buttons.RIGHT:
                changeSensors(MouseButtonSensor.MouseButton.MIDDLE_UP, worldCoordinates);
                break;
        }
        return false;

    }

    private void changeSensors(MouseButtonSensor.MouseButton event, Vector3 coordinates) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            MouseButtonSensor sensor = sensorEntity.getMouseButtonSensor();
            sensor.mouseEventSignal = event;
            sensor.positionSignal.set(coordinates.x, coordinates.y);

        }

    }

}


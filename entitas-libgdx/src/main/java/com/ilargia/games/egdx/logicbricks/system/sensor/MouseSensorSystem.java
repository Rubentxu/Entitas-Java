package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.ilargia.games.egdx.logicbricks.component.sensor.MouseSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorContext;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class MouseSensorSystem extends InputSensorSystem  {
    private final SensorContext sensorContex;
    private final Group<SensorEntity> sensorGroup;

    public MouseSensorSystem(Entitas entitas) {
        this.sensorContex = entitas.sensor;
        this.sensorGroup = sensorContex.getGroup(Matcher.AllOf(SensorMatcher.MouseSensor(), SensorMatcher.Link()));

//        sensorContex.getGroup(Matcher.AllOf(SensorMatcher.MouseSensor(), SensorMatcher.Link()))
//                .OnEntityAdded((IGroup<SensorEntity> group, SensorEntity entity, int index, IComponent component) ->{
//                    entity.getLink().targetEntity
//                    GameIndex.addGameEntity(entitas.game, entity.getCreationIndex(), entityB);
//                });


    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        boolean isActive = false;
        MouseSensor sensor = sensorEntity.getMouseSensor();
        if (sensor.mouseEventSignal != null && sensor.mouseEvent.equals(sensor.mouseEventSignal)) {
            isActive = true;
        }

        if (sensor.mouseEvent.equals(MouseSensor.MouseEvent.MOUSE_OVER)) {
//            isActive = isMouseOver(sensor.target, (int) sensor.positionSignal.x, (int) sensor.positionSignal.y);

        }

        if (!sensor.mouseEvent.equals(MouseSensor.MouseEvent.RIGHT_BUTTON_DOWN) && !sensor.mouseEvent.equals(MouseSensor.MouseEvent.MIDDLE_BUTTON_DOWN)
                && !sensor.mouseEvent.equals(MouseSensor.MouseEvent.LEFT_BUTTON_DOWN)) {
            sensor.mouseEventSignal = null;

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


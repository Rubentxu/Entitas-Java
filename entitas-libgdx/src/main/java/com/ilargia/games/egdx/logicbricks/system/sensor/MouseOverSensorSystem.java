package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.component.game.TextureView;
import com.ilargia.games.egdx.logicbricks.component.sensor.MouseOverSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

public class MouseOverSensorSystem extends InputSensorSystem {
    private final Group<SensorEntity> sensorGroup;
    private final Camera camera;
    private Rectangle testRectangle;
    private Vector3 worldCoordinates;

    public MouseOverSensorSystem(Entitas entitas, EngineGDX engine) {
        this.sensorGroup = entitas.sensor.getGroup(Matcher.AllOf(SensorMatcher.MouseOverSensor(), SensorMatcher.Link()));
        camera = engine.getCamera();
        testRectangle = new Rectangle();
        worldCoordinates = new Vector3(0, 0, 0);

    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        boolean isActive = false;
        MouseOverSensor sensor = sensorEntity.getMouseOverSensor();
        GameEntity target = Indexed.getTagEntity(sensor.targetTag);

        if (target != null) {
            TextureView view = target.getTextureView();
            RigidBody rigidBody = target.getRigidBody();
            if (view != null && rigidBody != null) {
                testRectangle.setPosition(rigidBody.body.getPosition().x, rigidBody.body.getPosition().y);
                testRectangle.setSize(view.bounds.extentsX * 2, view.bounds.extentsY * 2);
                isActive = testRectangle.contains(sensor.positionSignal.x, sensor.positionSignal.y);
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        worldCoordinates.set(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        MouseOverSensor sensor;
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            sensor = sensorEntity.getMouseOverSensor();
            sensor.positionSignal.set(worldCoordinates.x, worldCoordinates.y);
        }
        return false;

    }

}


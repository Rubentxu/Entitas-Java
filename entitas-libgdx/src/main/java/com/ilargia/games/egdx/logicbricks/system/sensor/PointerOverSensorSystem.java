package com.ilargia.games.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ilargia.games.egdx.api.managers.data.PointerState;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.InputManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.game.RigidBody;
import com.ilargia.games.egdx.logicbricks.component.game.TextureView;
import com.ilargia.games.egdx.logicbricks.component.sensor.PointerOverSensor;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorMatcher;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.group.Group;
import com.ilargia.games.entitas.matcher.Matcher;

import java.util.Set;

public class PointerOverSensorSystem extends SensorSystem implements IInitializeSystem {
    private Group<SensorEntity> sensorGroup;
    private EngineGDX engine;
    private Rectangle testRectangle;
    private InputManagerGDX inputManager;

    public PointerOverSensorSystem(Entitas entitas, EngineGDX engine) {
        this.sensorGroup = entitas.sensor.getGroup(Matcher.AllOf(SensorMatcher.PointerOverSensor(), SensorMatcher.Link()));
        this.engine = engine;
        testRectangle = new Rectangle();

    }

    @Override
    public void initialize() {
        inputManager = engine.getManager(InputManagerGDX.class);
    }

    @Override
    protected boolean query(SensorEntity sensorEntity, float deltaTime) {
        boolean isActive = false;
        PointerOverSensor sensor = sensorEntity.getPointerOverSensor();
        if (sensor.pointer > -1 && sensor.pointer < 5) {
            isActive = isOver(sensor, sensor.pointer);
        } else if (sensor.pointer == -1) {
            for (int i = 0; i < 5; i++) {
                isOver(sensor, i);
            }
        }
        return isActive;

    }

    private boolean isOver(PointerOverSensor sensor, int pointer) {
        PointerState<Vector2, Vector3> touchState = inputManager.getTouchState(pointer);
        if (touchState.down) {
            Set<GameEntity> targets = Indexed.getTagEntities(sensor.targetTag);
            for (GameEntity target : targets) {
                TextureView view = target.getTextureView();
                RigidBody rigidBody = target.getRigidBody();
                if (view != null && rigidBody != null) {
                    testRectangle.setPosition(rigidBody.body.getPosition().x, rigidBody.body.getPosition().y);
                    testRectangle.setSize(view.bounds.extentsX * 2, view.bounds.extentsY * 2);
                    return testRectangle.contains(touchState.coordinates.x, touchState.coordinates.y);
                }
            }
        }
        return false;
    }

    @Override
    public void execute(float deltaTime) {
        for (SensorEntity sensorEntity : sensorGroup.getEntities()) {
            process(sensorEntity, deltaTime);
        }
    }

}


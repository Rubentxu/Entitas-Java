package ilargia.egdx.logicbricks.system.sensor;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ilargia.egdx.api.managers.data.PointerState;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.impl.managers.InputManagerGDX;
import ilargia.egdx.logicbricks.component.game.RigidBody;
import ilargia.egdx.logicbricks.component.game.TextureView;
import ilargia.egdx.logicbricks.component.sensor.PointerOverSensor;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorEntity;
import ilargia.egdx.logicbricks.gen.sensor.SensorMatcher;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.group.Group;
import ilargia.entitas.matcher.Matcher;

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


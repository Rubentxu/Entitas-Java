package ilargia.egdx.impl.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import ilargia.egdx.api.GameController;
import ilargia.egdx.api.managers.InputManager;
import ilargia.egdx.api.managers.data.KeyState;
import ilargia.egdx.api.managers.data.PointerState;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.logicbricks.component.actuator.DragActuator;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.index.Indexed;
import ilargia.entitas.api.entitas.EntitasException;
import ilargia.entitas.factories.EntitasCollections;

import java.util.List;

public class InputManagerGDX implements InputManager<Vector2>, InputProcessor {

    private final EngineGDX engine;
    private final Entitas entitas;
    private List<GameController> controllers;
    private Camera camera;
    private boolean mouse;
    private DragActuator dragActuator;
    private InputStateData inputStateData;
    private World physics;
    private MouseJointDef jointDef;
    public MouseJoint []joints;

    // Mouse Properties
    public int scrollDelta;

    public InputManagerGDX(Entitas entitas, EngineGDX engine) {
        this.engine = engine;
        this.entitas = entitas;

        controllers = EntitasCollections.createList(InputManagerGDX.class);

        switch (Gdx.app.getType()) {
            case Android:
                mouse = false;
                break;
            case iOS:
                mouse = false;
                break;
            case Desktop:
                mouse = true;
                break;
            case WebGL:
                mouse = true;
                break;
            default:
                mouse = false;
        }
    }

    private QueryCallback queryCallback = fixture -> {
        for (PointerState pointerState : inputStateData.pointerStates) {
            testPoint(fixture, pointerState);
        }
        return false;
    };

    private void testPoint(Fixture fixture, PointerState<Vector2,Vector3> pointerState) {
        if (!fixture.testPoint(pointerState.coordinates.x, pointerState.coordinates.y))
            return;
        Integer index = (Integer) fixture.getBody().getUserData();
        GameEntity entity =  Indexed.getInteractiveEntity(index);
        if(entity.isDraggable()) {
            jointDef.bodyB = fixture.getBody();
            jointDef.target.set(pointerState.coordinates.x, pointerState.coordinates.y);
            joints[pointerState.pointer] = (MouseJoint) physics.createJoint(jointDef);
        }
    }

    @Override
    public void initialize() {
        if (engine.getManager(PhysicsManagerGDX.class) == null) throw new EntitasException("InputManagerGDX",
                "InputManagerGDX needs load PreferencesManagerGDX on the engine");
        this.camera = engine.getCamera();
        this.physics = engine.getPhysics();
        joints =  new MouseJoint[5];
        inputStateData = new InputStateData();

    }

    @Override
    public void addController(GameController gameController) {
        controllers.add(gameController);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return inputStateData.keyStates[key].pressed;
    }

    @Override
    public boolean isKeyDown(int key) {
        return inputStateData.keyStates[key].down;
    }

    @Override
    public boolean isKeyReleased(int key) {
        return inputStateData.keyStates[key].released;
    }

    @Override
    public boolean isTouchPressed(int pointer) {
        return inputStateData.pointerStates[pointer].pressed;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return inputStateData.pointerStates[pointer].down;
    }

    @Override
    public boolean isTouchReleased(int pointer) {
        return inputStateData.pointerStates[pointer].released;
    }

    @Override
    public Vector2 touchCoordinates(int pointer) {
        return inputStateData.pointerStates[pointer].coordinates;
    }

    @Override
    public Vector2 touchDisplacement(int pointer) {
        return inputStateData.pointerStates[pointer].displacement;
    }

    @Override
    public int getScrollDelta() {
        return scrollDelta;
    }

    @Override
    public PointerState getTouchState(int pointer) {
        if (inputStateData.pointerStates.length > pointer) {
            return inputStateData.pointerStates[pointer];
        } else {
            return inputStateData.pointerStates[0];
        }
    }

    @Override
    public void update(float deltaTime) {
        //for every keystate, set pressed and released to false.
        for (int i = 0; i < 256; i++) {
            KeyState k = inputStateData.keyStates[i];
            k.pressed = false;
            k.released = false;
        }

        for (int i = 0; i < inputStateData.pointerStates.length; i++) {
            PointerState<Vector2, Vector3> t = inputStateData.pointerStates[i];
            if(t.down && !t.pressed) t.clickTime += deltaTime;
            t.pressed = false;
            t.released = false;
            t.displacement.x = 0;
            t.displacement.y = 0;
        }

        if(entitas.actuator.hasDragActuator() && jointDef == null) {
            this.dragActuator = entitas.actuator.getDragActuator();
            jointDef = new MouseJointDef();
            jointDef.bodyA = Indexed.getInteractiveEntity(dragActuator.targetEntity).getRigidBody().body;
            jointDef.collideConnected = dragActuator.collideConnected;
            jointDef.maxForce = dragActuator.maxForce;
        }

        for (GameController controller : controllers) {
            controller.update(this, entitas);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        //I need to store the state of the key being held down as well as pressed
        inputStateData.keyStates[keycode].pressed = true;
        inputStateData.keyStates[keycode].down = true;

        //every overridden method needs a return value. I won't be utilizing this but it can be used for error handling.
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        //the key was released, I need to set it's down state to false and released state to true
        inputStateData.keyStates[keycode].down = false;
        inputStateData.keyStates[keycode].released = true;
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int indexPointer = (mouse) ? button : pointer;
        //set the state of all touch state events
        if (indexPointer < inputStateData.pointerStates.length) {
            PointerState<Vector2,Vector3> t = inputStateData.pointerStates[indexPointer];
            t.down = true;
            t.pressed = true;

            //recording last position for displacement values
            t.lastPosition.x = t.coordinates.x;
            t.lastPosition.y = t.coordinates.y;

            t.worldCoordinates.set(screenX, screenY, 0);
            camera.unproject(t.worldCoordinates);

            //store the coordinates of this touch event
            t.coordinates.x = t.worldCoordinates.x;
            t.coordinates.y = t.worldCoordinates.y;

            physics.QueryAABB(queryCallback, t.coordinates.x, t.coordinates.y, t.coordinates.x, t.coordinates.y);

        }
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int indexPointer = (mouse) ? button : pointer;
        //set the state of all touch state events
        if (indexPointer < inputStateData.pointerStates.length) {
            PointerState<Vector2, Vector3> t = inputStateData.pointerStates[indexPointer];
            t.worldCoordinates.set(screenX, screenY, 0);
            camera.unproject(t.worldCoordinates);

            t.down = false;
            t.released = true;
            t.clickTime = 0;
            t.coordinates.x = t.worldCoordinates.x;
            t.coordinates.y = t.worldCoordinates.y;
            if(joints[indexPointer] != null) {
                physics.destroyJoint(joints[indexPointer]);
                joints[indexPointer] = null;
            }

        }
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!mouse) dragged(screenX, screenY, pointer);
        else {
            for (int i = 0; i < inputStateData.pointerStates.length; i++) {
                if(inputStateData.pointerStates[i].down) dragged(screenX, screenY, i);
            }
        }
        return false;

    }

    public void dragged(int screenX, int screenY, int pointer) {

        PointerState<Vector2,Vector3> t = inputStateData.pointerStates[pointer];
        //get altered coordinates
        t.worldCoordinates.set(screenX, screenY, 0);
        camera.unproject(t.worldCoordinates);
        //set coordinates of this touchstate
        t.coordinates.x = t.worldCoordinates.x;
        t.coordinates.y = t.worldCoordinates.y;
        //calculate the displacement of this touchstate based on
        //the information from the last frame's position
        t.displacement.x = t.coordinates.x - t.lastPosition.x;
        t.displacement.y = t.coordinates.y - t.lastPosition.y;
        //store the current position into last position for next frame.
        t.lastPosition.x = t.coordinates.x;
        t.lastPosition.y = t.coordinates.y;
        if(joints[pointer] != null) {
            joints[pointer].setTarget(t.coordinates);

        }

    }


    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        scrollDelta = i;
        return false;
    }

    @Override
    public void dispose() {

    }

}

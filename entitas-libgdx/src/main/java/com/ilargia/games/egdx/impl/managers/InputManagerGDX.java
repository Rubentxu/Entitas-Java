package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.ilargia.games.egdx.api.GameController;
import com.ilargia.games.egdx.api.managers.InputManager;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.component.actuator.DragActuator;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.EntitasException;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.List;

public class InputManagerGDX implements InputManager, InputProcessor {

    public enum InputButton { Left, Right, Middle }

    private final EngineGDX engine;
    private final Entitas entitas;
    private KeyState[] keyStates;
    private PointerState[] touchStates;
    private List<GameController> controllers;
    private Camera camera;
    private boolean mouse;
    private World physics;
    private MouseJointDef jointDef;
    private DragActuator dragActuator;

    // Mouse Properties
    public InputButton button;
    public int scrollDelta;

    public InputManagerGDX(Entitas entitas, EngineGDX engine) {
        this.engine = engine;
        this.entitas = entitas;
        keyStates = new KeyState[256];
        touchStates = new PointerState[5];

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


    @Override
    public void initialize() {
        if (engine.getManager(PhysicsManagerGDX.class) == null) throw new EntitasException("InputManagerGDX",
                "InputManagerGDX needs load PreferencesManagerGDX on the engine");
        this.camera = engine.getCamera();
        this.physics = engine.getPhysics();
        Gdx.input.setInputProcessor(this);
        //create the initial state of every key on the keyboard.
        //There are 256 keys available which are all represented as integers.
        for (int i = 0; i < 256; i++) {
            keyStates[i] = new KeyState(i);
        }

        for (int i = 0; i < 5; i++) {
            touchStates[i] = new PointerState(0, 0, i);
        }

    }

    @Override
    public void addController(GameController gameController) {
        controllers.add(gameController);
    }

    @Override
    public boolean isKeyPressed(int key) {
        return keyStates[key].pressed;
    }

    @Override
    public boolean isKeyDown(int key) {
        return keyStates[key].down;
    }

    @Override
    public boolean isKeyReleased(int key) {
        return keyStates[key].released;
    }

    @Override
    public boolean isTouchPressed(int pointer) {
        return touchStates[pointer].pressed;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchStates[pointer].down;
    }

    @Override
    public boolean isTouchReleased(int pointer) {
        return touchStates[pointer].released;
    }

    public Vector2 touchCoordinates(int pointer) {
        return touchStates[pointer].coordinates;
    }

    public Vector2 touchDisplacement(int pointer) {
        return touchStates[pointer].displacement;
    }

    public InputButton getMouseButton(int pointer) {
        switch (pointer) {
            case 0:
                return InputButton.Left;
            case 1:
                return InputButton.Middle;
            case 2:
                return InputButton.Right;
            default:
                return InputButton.Left;
        }
    }

    public int getScrollDelta() {
        return scrollDelta;
    }

    public PointerState getTouchState(int pointer) {
        if (touchStates.length > pointer) {
            return touchStates[pointer];
        } else {
            return null;
        }
    }

    @Override
    public void update(float deltaTime) {

        //for every keystate, set pressed and released to false.
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates[i];
            k.pressed = false;
            k.released = false;
        }

        for (int i = 0; i < touchStates.length; i++) {
            PointerState t = touchStates[i];
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
        //this function only gets called once when an event is fired. (even if this key is being held down)

        //I need to store the state of the key being held down as well as pressed
        keyStates[keycode].pressed = true;
        keyStates[keycode].down = true;

        //every overridden method needs a return value. I won't be utilizing this but it can be used for error handling.
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        //the key was released, I need to set it's down state to false and released state to true
        keyStates[keycode].down = false;
        keyStates[keycode].released = true;
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
        if (indexPointer < touchStates.length) {
            PointerState t = touchStates[indexPointer];
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

            physics.QueryAABB(t.queryCallback, t.coordinates.x, t.coordinates.y, t.coordinates.x, t.coordinates.y);


        }
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int indexPointer = (mouse) ? button : pointer;
        //set the state of all touch state events
        if (indexPointer < touchStates.length) {
            PointerState t = touchStates[indexPointer];
            t.worldCoordinates.set(screenX, screenY, 0);
            camera.unproject(t.worldCoordinates);

            t.down = false;
            t.released = true;
            t.clickTime = 0;
            t.coordinates.x = t.worldCoordinates.x;
            t.coordinates.y = t.worldCoordinates.y;
            if(t.joint != null) {
                physics.destroyJoint(t.joint);
                t.joint = null;
            }

        }
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!mouse) dragged(screenX, screenY, pointer);
        else {
            for (int i = 0; i < touchStates.length; i++) {
                if(touchStates[i].down) dragged(screenX, screenY, i);
            }
        }
        return false;

    }

    public void dragged(int screenX, int screenY, int pointer) {

        PointerState t = touchStates[pointer];
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
        if(t.joint != null) {
            t.joint.setTarget(t.coordinates);

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

    public class InputState {
        public boolean pressed = false;
        public boolean down = false;
        public boolean released = false;
        public boolean notChanged = false;

    }

    public class KeyState extends InputState {
        //the keyboard key of this object represented as an integer.
        public int key;

        public KeyState(int key) {
            this.key = key;
        }
    }

    public class PointerState extends InputState {
        //keep track of which finger this object belongs to
        public final int pointer;
        //coordinates of this finger/mouse
        public Vector3 worldCoordinates;
        public Vector2 coordinates;
        //track the displacement of this finger/mouse
        public Vector2 lastPosition;
        public Vector2 displacement;
        public float clickTime; // The last time a click event was sent out (used for double-clicks)
        public int clickCount; // Number of clicks in a row. 2 for a double-click for example.
        public MouseJoint joint;

        public PointerState(int coord_x, int coord_y, int pointer) {
            this.pointer = pointer;
            worldCoordinates = new Vector3(0, 0, 0);
            coordinates = new Vector2(coord_x, coord_y);
            lastPosition = new Vector2(0, 0);
            displacement = new Vector2(lastPosition.x, lastPosition.y);
            clickTime = 0.0f;
            clickCount = 0;

        }

        private QueryCallback queryCallback = fixture -> {
            if (!fixture.testPoint(coordinates.x, coordinates.y))
                return true;

            jointDef.bodyB = fixture.getBody();
            jointDef.target.set(coordinates.x, coordinates.y);
            joint = (MouseJoint) physics.createJoint(jointDef);
            return false;
        };

        @Override
        public String toString() {
            return "TouchState{" +
                    "pointer=" + pointer +
                    ", coordinates=" + coordinates +
                    ", lastPosition=" + lastPosition +
                    ", displacement=" + displacement +
                    "InputState=" + super.toString()+
                    '}';
        }

    }



}

package com.ilargia.games.egdx.impl.managers;

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
import com.ilargia.games.egdx.api.GameController;
import com.ilargia.games.egdx.api.managers.InputManager;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.component.actuator.DragActuator;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.api.EntitasException;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.List;
import java.util.Set;

public class InputManagerGDX implements InputManager, InputProcessor {

    private final EngineGDX engine;
    private final Entitas entitas;
    private KeyState[] keyStates;
    private TouchState[] touchStates;
    private List<GameController> controllers;
    private Camera camera;
    private Vector3 worldCoordinates;
    private Vector2 tmp2 = new Vector2();
    private boolean mouse;
    private World physics;
    private MouseJointDef jointDef;
    private DragActuator dragActuator;

    public InputManagerGDX(Entitas entitas, EngineGDX engine) {
        this.engine = engine;
        this.entitas = entitas;
        keyStates = new KeyState[256];
        touchStates = new TouchState[5];
        worldCoordinates = new Vector3(0, 0, 0);
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
            touchStates[i] = new TouchState(0, 0, i);
        }

    }

    private int coordinateX(int screenX) {
        return (int) (screenX);
    }

    private int coordinateY(int screenY) {
        return (int) screenY;
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

    public TouchState getTouchState(int pointer) {
        if (touchStates.length > pointer) {
            return touchStates[pointer];
        } else {
            return null;
        }
    }

    public void update() {
        for (GameController controller : controllers) {
            controller.update(this, entitas);
        }
        //for every keystate, set pressed and released to false.
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates[i];
            k.pressed = false;
            k.released = false;
        }

        for (int i = 0; i < touchStates.length; i++) {
            TouchState t = touchStates[i];
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
        //get altered coordinates
        worldCoordinates.set(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
        int coord_x = coordinateX((int) worldCoordinates.x);
        int coord_y = coordinateY((int) worldCoordinates.y);

        int indexPointer = (mouse) ? button : pointer;
        //set the state of all touch state events
        if (indexPointer < touchStates.length) {
            TouchState t = touchStates[indexPointer];
            t.down = true;
            t.pressed = true;

            //store the coordinates of this touch event
            t.coordinates.x = coord_x;
            t.coordinates.y = coord_y;
            //recording last position for displacement values
            t.lastPosition.x = coord_x;
            t.lastPosition.y = coord_y;
            if(entitas.actuator.hasDragActuator()) physics.QueryAABB(t.queryCallback, worldCoordinates.x, worldCoordinates.y,
                    worldCoordinates.x, worldCoordinates.y);


        }
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        int indexPointer = (mouse) ? button : pointer;
        //set the state of all touch state events
        if (indexPointer < touchStates.length) {
            worldCoordinates.set(screenX, screenY, 0);
            camera.unproject(worldCoordinates);
            int coord_x = coordinateX((int) worldCoordinates.x);
            int coord_y = coordinateY((int) worldCoordinates.y);

            TouchState t = touchStates[indexPointer];
            t.down = false;
            t.released = true;
            t.coordinates.x = coord_x;
            t.coordinates.y = coord_y;
            if(entitas.actuator.hasDragActuator() && t.joint != null) {
                physics.destroyJoint(t.joint);
                t.joint = null;
            }

        }
        return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        worldCoordinates.set(screenX, screenY, 0);
        camera.unproject(worldCoordinates);

        if(!mouse) dragged(pointer);
        else {
            for (int i = 0; i < touchStates.length; i++) {
                if(touchStates[i].down) dragged(i);
            }
        }
        return false;

    }

    public void dragged(int pointer) {
        //get altered coordinates
        LogManagerGDX.debug("InputManager", "touchUp pointer %d, isMouse %s" , pointer, mouse);
        int coord_x = coordinateX((int) worldCoordinates.x);
        int coord_y = coordinateY((int) worldCoordinates.y);

        TouchState t = touchStates[pointer];
        //set coordinates of this touchstate
        t.coordinates.x = coord_x;
        t.coordinates.y = coord_y;
        //calculate the displacement of this touchstate based on
        //the information from the last frame's position
        t.displacement.x = coord_x - t.lastPosition.x;
        t.displacement.y = coord_y - t.lastPosition.y;
        //store the current position into last position for next frame.
        t.lastPosition.x = coord_x;
        t.lastPosition.y = coord_y;
        if(entitas.actuator.hasDragActuator() && t.joint != null) {
            t.joint.setTarget(tmp2.set(worldCoordinates.x, worldCoordinates.y));

        }

    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }


    @Override
    public void dispose() {

    }

    public class InputState {
        public boolean pressed = false;
        public boolean down = false;
        public boolean released = false;

        @Override
        public String toString() {
            return "InputState{" +
                    "pressed=" + pressed +
                    ", down=" + down +
                    ", released=" + released +
                    '}';
        }
    }

    public class KeyState extends InputState {
        //the keyboard key of this object represented as an integer.
        public int key;

        public KeyState(int key) {
            this.key = key;
        }
    }

    public class TouchState extends InputState {
        //keep track of which finger this object belongs to
        public final int pointer;
        //coordinates of this finger/mouse
        public Vector2 coordinates;
        //track the displacement of this finger/mouse
        public Vector2 lastPosition;
        public Vector2 displacement;
        private MouseJoint joint;

        private QueryCallback queryCallback = fixture -> {
            if (!fixture.testPoint(coordinates.x, coordinates.y))
                return true;

            jointDef.bodyB = fixture.getBody();
            jointDef.target.set(coordinates.x, coordinates.y);
            joint = (MouseJoint) physics.createJoint(jointDef);
            return false;
        };

        public TouchState(int coord_x, int coord_y, int pointer) {
            this.pointer = pointer;
            coordinates = new Vector2(coord_x, coord_y);
            lastPosition = new Vector2(0, 0);
            displacement = new Vector2(lastPosition.x, lastPosition.y);

        }

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

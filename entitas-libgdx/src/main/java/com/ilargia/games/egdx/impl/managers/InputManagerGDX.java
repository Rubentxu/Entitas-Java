package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ilargia.games.egdx.api.managers.InputManager;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.entitas.api.EntitasException;

public class InputManagerGDX implements InputManager, InputProcessor {

    private final EngineGDX engine;
    public KeyState[] keyStates;
    public TouchState[] touchStates;
    private PreferencesManagerGDX preferences;
    private Camera camera;
    private Vector3 worldCoordinates;
    private boolean mouse;

    public InputManagerGDX(EngineGDX engine) {
        this.engine = engine;
        keyStates = new KeyState[255];
        touchStates = new TouchState[5];
        worldCoordinates = new Vector3(0, 0, 0);
     
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
        preferences = engine.getManager(PreferencesManagerGDX.class);
        camera = engine.getCamera();

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
        return (int) (screenX - preferences.GAME_WIDTH / 2);
    }

    private int coordinateY(int screenY) {
        return (int) (preferences.GAME_HEIGHT / 2 - screenY);
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
        worldCoordinates = new Vector3(screenX, screenY, 0);
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


        }
        return false;

    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        int indexPointer = (mouse) ? button : pointer;
        //set the state of all touch state events
        if (indexPointer < touchStates.length) {
            worldCoordinates = new Vector3(screenX, screenY, 0);
            camera.unproject(worldCoordinates);
            int coord_x = coordinateX((int) worldCoordinates.x);
            int coord_y = coordinateY((int) worldCoordinates.y);

            TouchState t = touchStates[indexPointer];
            t.down = false;
            t.released = true;
            t.coordinates.x = coord_x;
            t.coordinates.y = coord_y;

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
        //get altered coordinates
        worldCoordinates = new Vector3(screenX, screenY, 0);
        camera.unproject(worldCoordinates);
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
        private Vector2 lastPosition;
        public Vector2 displacement;

        public TouchState(int coord_x, int coord_y, int pointer) {
            this.pointer = pointer;
            coordinates = new Vector2(coord_x, coord_y);
            lastPosition = new Vector2(0, 0);
            displacement = new Vector2(lastPosition.x, lastPosition.y);
        }
    }

}

package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;

public class InputStateData {

    public KeyState[] keyStates;
    public PointerState[] pointerStates;
    // Mouse Properties
    public int scrollDelta;

    public InputStateData() {
        keyStates = new KeyState[256];
        pointerStates = new PointerState[5];

        //create the initial state of every key on the keyboard.
        //There are 256 keys available which are all represented as integers.
        for (int i = 0; i < 256; i++) {
            keyStates[i] = new KeyState(i);
        }

        for (int i = 0; i < 5; i++) {
            pointerStates[i] = new PointerState(0, 0, i);
        }
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

        public PointerState(int coord_x, int coord_y, int pointer) {
            this.pointer = pointer;
            worldCoordinates = new Vector3(0, 0, 0);
            coordinates = new Vector2(coord_x, coord_y);
            lastPosition = new Vector2(0, 0);
            displacement = new Vector2(lastPosition.x, lastPosition.y);
            clickTime = 0.0f;
            clickCount = 0;

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

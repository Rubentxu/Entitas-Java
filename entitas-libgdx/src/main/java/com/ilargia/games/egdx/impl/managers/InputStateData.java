package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.ilargia.games.egdx.api.managers.data.InputState;
import com.ilargia.games.egdx.api.managers.data.KeyState;
import com.ilargia.games.egdx.api.managers.data.PointerState;

public class InputStateData {

    public KeyState[] keyStates;
    public PointerState<Vector2,Vector3>[] pointerStates;
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
            pointerStates[i] = new PointerState(i);
            pointerStates[i].worldCoordinates = new Vector3(0, 0, 0);
            pointerStates[i].coordinates = new Vector2(0, 0);
            pointerStates[i].lastPosition = new Vector2(0, 0);
            pointerStates[i].displacement = new Vector2(0,0);
        }
    }



}

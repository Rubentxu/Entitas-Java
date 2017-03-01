package com.ilargia.games.core.data;


public enum StateCharacter {

    IDLE, WALKING, JUMPING, DYING, FALL, SWIMMING, PROPULSION, WIN, HURT(1f), HIT(1f), DEAD(1f);

    public float stateTimeMin;

    StateCharacter() {
        this.stateTimeMin = 0.1f;
    }

    StateCharacter(float stateTimeMin) {
        this.stateTimeMin = stateTimeMin;
    }

    public float getStateTimeMin() {
        return this.stateTimeMin;
    }
}

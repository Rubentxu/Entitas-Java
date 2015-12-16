package com.ilargia.games.entitas.events;

public enum GroupEventType {
    OnEntityAdded,
    OnEntityRemoved,
    OnEntityAddedOrRemoved;

    public static GroupEventType forValue(int value) {
        return values()[value];
    }

    public int getValue() {
        return this.ordinal();
    }
}
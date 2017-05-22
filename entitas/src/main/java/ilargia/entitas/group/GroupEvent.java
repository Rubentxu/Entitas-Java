package ilargia.entitas.group;

public enum GroupEvent {
    Added,
    Removed,
    AddedOrRemoved;

    public static GroupEvent forValue(int value) {
        return values()[value];
    }

    public int getValue() {
        return this.ordinal();
    }
}
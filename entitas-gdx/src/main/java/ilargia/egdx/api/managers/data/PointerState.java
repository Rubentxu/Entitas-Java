package ilargia.egdx.api.managers.data;

public class PointerState<Vector2, Vector3> extends InputState {
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

    public PointerState(int pointer) {
        this.pointer = pointer;
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
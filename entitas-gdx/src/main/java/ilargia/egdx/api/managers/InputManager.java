package ilargia.egdx.api.managers;

import ilargia.egdx.api.GameController;
import ilargia.egdx.api.managers.data.PointerState;

public interface InputManager<Coordinates> extends Manager {

    public enum InputButton { Left, Right, Middle }

    public void addController(GameController gameController);

    public boolean isKeyPressed(int key);

    public boolean isKeyDown(int key);

    public boolean isKeyReleased(int key);

    public boolean isTouchPressed(int pointer);

    public boolean isTouchDown(int pointer);

    public boolean isTouchReleased(int pointer);

    public Coordinates touchCoordinates(int pointer);

    public Coordinates touchDisplacement(int pointer);

    default public InputManager.InputButton getMouseButton(int pointer) {
        switch (pointer) {
            case 0:
                return InputManager.InputButton.Left;
            case 1:
                return InputManager.InputButton.Middle;
            case 2:
                return InputManager.InputButton.Right;
            default:
                return InputManager.InputButton.Left;
        }
    }

    public PointerState getTouchState(int pointer);

    public int getScrollDelta();

    public void update(float deltaTime);


}

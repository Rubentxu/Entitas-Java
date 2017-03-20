package com.ilargia.games.egdx.api.managers;


import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.egdx.api.GameController;

public interface InputManager extends Manager {

    public void addController(GameController gameController);

    public boolean isKeyPressed(int key);

    public boolean isKeyDown(int key);

    public boolean isKeyReleased(int key);

    public boolean isTouchPressed(int pointer);

    public boolean isTouchDown(int pointer);

    public boolean isTouchReleased(int pointer);


}

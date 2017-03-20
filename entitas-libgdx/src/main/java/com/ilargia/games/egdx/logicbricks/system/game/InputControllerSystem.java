package com.ilargia.games.egdx.logicbricks.system.game;


import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.InputManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.system.IExecuteSystem;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.group.Group;

public class InputControllerSystem implements IInitializeSystem, IExecuteSystem {
    private final Engine engine;
    private InputManagerGDX inputManager;

    public InputControllerSystem(Engine engine) {
        this.engine = engine;
    }

    @Override
    public void initialize() {
        this.inputManager = engine.getManager(InputManagerGDX.class);
    }

    @Override
    public void execute(float deltaTime) {
       inputManager.update();
    }

}

package com.ilargia.games.egdx.logicbricks.system.game;


import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.impl.managers.InputManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;

public class AddInputControllerSystem extends ReactiveSystem<GameEntity> implements IInitializeSystem {
    private final Engine engine;
    private InputManagerGDX inputManager;

    public AddInputControllerSystem(Entitas entitas, Engine engine) {
        super(entitas.game);
        this.engine = engine;

    }

    @Override
    public void initialize() {
        this.inputManager = engine.getManager(InputManagerGDX.class);
    }


    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasInputController();
    }

    @Override
    protected Collector getTrigger(IContext context) {
        return context.createCollector(GameMatcher.InputController());
    }

    @Override
    protected void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            inputManager.addController(e.getInputController().controller);
        }
    }

}

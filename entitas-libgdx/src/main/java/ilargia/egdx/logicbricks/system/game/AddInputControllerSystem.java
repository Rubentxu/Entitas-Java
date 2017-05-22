package ilargia.egdx.logicbricks.system.game;


import ilargia.egdx.api.Engine;
import ilargia.egdx.impl.managers.InputManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.game.GameMatcher;
import ilargia.entitas.api.IContext;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.systems.ReactiveSystem;

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

package com.ilargia.games.systems;

import com.ilargia.games.EntityIndexExtension;
import com.ilargia.games.components.Input;
import com.ilargia.games.core.*;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class ProcessInputSystem extends ReactiveSystem<InputEntity> {

    private final Entitas entitas;
    private InputContext context;

    public ProcessInputSystem(Entitas entitas) {
        super(entitas.input);
        this.context = entitas.input;
        this.entitas = entitas;
    }

    @Override
    protected Collector<InputEntity> getTrigger(IContext<InputEntity> context) {
        return context.createCollector(InputMatcher.Input());
    }

    @Override
    protected boolean filter(InputEntity entity) {
        return entity.hasInput();
    }

    @Override
    protected void execute(List<InputEntity> entities) {
        InputEntity inputEntity = entities.get(0);
        Input input = inputEntity.getInput();

        Set<GameEntity> interactives = EntityIndexExtension.getEntitiesWithPosition(entitas.game, input.x, input.y)
                .stream().filter(e -> e.isInteractive()).collect(Collectors.toSet());

        for (GameEntity e : interactives) {
            e.setDestroy(true);
        }
    }

}

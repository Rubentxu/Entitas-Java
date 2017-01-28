package com.ilargia.games.systems;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.EntityIndexExtension;
import com.ilargia.games.components.Input;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISetPools;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class ProcessInputSystem implements ISetPools<Pools>, IReactiveSystem<Entity> {
    private Pools _pools;


    @Override
    public void setPools(Pools pools) {
        _pools = pools;
    }

    @Override
    public TriggerOnEvent getTrigger() {
        return InputMatcher.Input().OnEntityAdded();
    }

    @Override
    public void execute(Array<Entity> entities) {
        Entity inputEntity = entities.get(0);
        Input input = inputEntity.getInput();

        ObjectSet.ObjectSetIterator<Entity> it = EntityIndexExtension.getEntitiesWithPosition(_pools.core, input.x, input.y).iterator();
        Entity entity;
        Array<Entity> interactives = new Array<>();
        while (it.hasNext()) {
            entity = it.next();
            if (entity.isInteractive())
                interactives.add(entity);
        }

        for (Entity e : interactives) {
            e.setDestroy(true);
        }
    }
}

package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;

public abstract class AbstractEntityIndex<K, E extends Entity> implements IEntityIndex {

    private EventBus<E> _eventBus;
    protected Group<E> _group;
    protected Func<E, IComponent, K> _key;
    protected GroupChanged<E> onEntityAdded = (Group<E> group, E entity, int index, IComponent component) -> {
        addEntity(entity, component);
    };
    protected GroupChanged<E> onEntityRemoved = (Group<E> group, E entity, int index, IComponent component) -> {
        removeEntity(entity, component);
    };

    protected AbstractEntityIndex(Group<E> group, Func<E, IComponent, K> key, EventBus<E> eventBus) {
        _group = group;
        _key = key;
        _eventBus = eventBus;

    }

    @Override
    public void activate() {
        _eventBus.OnEntityAdded(_group).addListener(onEntityAdded);
        _eventBus.OnEntityRemoved(_group).addListener(onEntityRemoved);

    }

    @Override
    public void deactivate() {
        _eventBus.OnEntityAdded(_group).removeListener(onEntityAdded);
        _eventBus.OnEntityRemoved(_group).removeListener(onEntityRemoved);
        clear();

    }

    protected void indexEntities(Group<E> group) {
        E[] entities = group.getEntities();
        for (int i = 0; i < entities.length; i++) {
            addEntity(entities[i], null);
        }
    }

    protected abstract void addEntity(E entity, IComponent component);

    protected abstract void removeEntity(E entity, IComponent component);

    protected abstract void clear();

    public interface Func<E, IComponent, K> {
        K getKey(E entity, IComponent component);
    }

}

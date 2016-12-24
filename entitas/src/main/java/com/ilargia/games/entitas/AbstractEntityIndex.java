package com.ilargia.games.entitas;

import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.IEntityIndex;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;

public abstract class AbstractEntityIndex<K, E extends Entity> implements IEntityIndex {

    protected Group<E> _group;
    protected Func<E, IComponent, K> _key;
    protected GroupChanged<E> onEntityAdded = (Group<E> group, E entity, int index, IComponent component) -> {
        addEntity(entity, component);
    };
    protected GroupChanged<E> onEntityRemoved = (Group<E> group, E entity, int index, IComponent component) -> {
        removeEntity(entity, component);
    };

    protected AbstractEntityIndex(Group<E> group, Func<E, IComponent, K> key) {
        _group = group;
        _key = key;

    }

    @Override
    public void activate() {
        _group.OnEntityAdded = onEntityAdded;
        _group.OnEntityRemoved = onEntityRemoved;

    }

    @Override
    public void deactivate() {
        _group.OnEntityAdded = null;
        _group.OnEntityRemoved = null;
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

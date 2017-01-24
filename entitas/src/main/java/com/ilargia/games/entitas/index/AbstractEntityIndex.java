package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IEntityIndex;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.events.EventBus;

public abstract class AbstractEntityIndex<TEntity extends IEntity, TKey> implements IEntityIndex {

    protected IGroup<TEntity> _group;
    protected Func<TEntity, IComponent, TKey> _key;
    protected GroupChanged<TEntity> onEntityAdded = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
        addEntity(entity, component);
    };
    protected GroupChanged<TEntity> onEntityRemoved = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
        removeEntity(entity, component);
    };
    private EventBus<TEntity> _eventBus;

    protected AbstractEntityIndex(IGroup<TEntity> group, Func<TEntity, IComponent, TKey> key, EventBus<TEntity> eventBus) {
        _group = group;
        _key = key;
        _eventBus = eventBus;

    }

    @Override
    public void activate() {
        _eventBus.OnEntityAdded.addListener(onEntityAdded);
        _eventBus.OnEntityRemoved.addListener(onEntityRemoved);

    }

    @Override
    public void deactivate() {
        _eventBus.OnEntityAdded.removeListener(onEntityAdded);
        _eventBus.OnEntityRemoved.removeListener(onEntityRemoved);
        clear();

    }

    protected void indexEntities(IGroup<TEntity> group) {
        TEntity[] entities = group.getEntities();
        for (int i = 0; i < entities.length; i++) {
            addEntity(entities[i], null);
        }
    }

    protected abstract void addEntity(TEntity entity, IComponent component);

    protected abstract void removeEntity(TEntity entity, IComponent component);

    protected abstract void clear();

    public interface Func<TEntity, IComponent, TKey> {
        TKey getKey(TEntity entity, IComponent component);
    }

}

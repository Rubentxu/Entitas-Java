package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IEntityIndex;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.group.Group;

import java.util.UUID;

public abstract class AbstractEntityIndex<TEntity extends Entity, TKey> implements IEntityIndex {

    protected UUID id = UUID.randomUUID();
    protected Group<TEntity> _group;
    protected Func<TEntity, IComponent, TKey> _key;
    protected GroupChanged<TEntity> onEntityAdded = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
        addEntity(entity, component);
    };
    protected GroupChanged<TEntity> onEntityRemoved = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
        removeEntity(entity, component);
    };


    protected AbstractEntityIndex(IGroup<TEntity> group, Func<TEntity, IComponent, TKey> key) {
        _group = (Group<TEntity>) group;
        _key = key;

    }

    @Override
    public void activate() {
        _group.OnEntityAdded(onEntityAdded);
        _group.OnEntityRemoved(onEntityRemoved);

    }

    @Override
    public void deactivate() {
        _group.OnEntityAdded.remove(onEntityAdded);
        _group.OnEntityRemoved.remove(onEntityRemoved);
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

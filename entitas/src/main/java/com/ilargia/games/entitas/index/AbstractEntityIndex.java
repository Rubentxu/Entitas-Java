package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntityIndex;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.group.Group;

public abstract class AbstractEntityIndex<TEntity extends Entity, TKey> implements IEntityIndex {

    protected Group<TEntity> _group;
    protected Func<TEntity, IComponent, TKey> _key;
    protected Func<TEntity, IComponent, TKey[]> _keys;
    protected boolean _isSingleKey;

    protected GroupChanged<TEntity> onEntityAdded = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
        if(_isSingleKey) {
            addEntity(_key.getKey(entity, component), entity);
        } else {
            TKey[] keys = _keys.getKey(entity, component);
            for(int i = 0; i < keys.length; i++) {
                addEntity(keys[i], entity);
            }
        }
    };
    protected GroupChanged<TEntity> onEntityRemoved = (IGroup<TEntity> group, TEntity entity, int index, IComponent component) -> {
        if(_isSingleKey) {
            removeEntity(_key.getKey(entity, component), entity);
        } else {
            TKey[] keys = _keys.getKey(entity, component);
            for(int i = 0; i < keys.length; i++) {
                removeEntity(keys[i], entity);
            }
        }
    };


    protected AbstractEntityIndex(Func<TEntity, IComponent, TKey> key, IGroup<TEntity> group) {
        _group = (Group<TEntity>) group;
        _key = key;
        _isSingleKey = true;

    }

    protected AbstractEntityIndex(IGroup<TEntity> group, Func<TEntity, IComponent, TKey[]> keys) {
        _group = (Group<TEntity>) group;
        _keys = keys;
        _isSingleKey = false;
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
            TEntity entity = entities[i];
            if(_isSingleKey) {
                addEntity(_key.getKey(entity, null), entity);
            } else {
                for (TKey k : _keys.getKey(entity, null)) {
                    addEntity(k, entity);
                }
            }

        }
    }

    protected abstract void addEntity(TKey key, TEntity entity);

    protected abstract void removeEntity(TKey key, TEntity entity);

    protected abstract void clear();

    public interface Func<TEntity, IComponent, TKey> {
        TKey getKey(TEntity entity, IComponent component);
    }


}

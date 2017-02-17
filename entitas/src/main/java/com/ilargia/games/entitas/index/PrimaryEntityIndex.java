package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;

public class PrimaryEntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {

    private Map<TKey, TEntity> _index; //Object2ObjectArrayMap

    public PrimaryEntityIndex(Func<TEntity, IComponent, TKey> key, IGroup group) {
        super(key, group);
        _index = Collections.createMap(Object.class, Object.class);
        activate();
    }

    public PrimaryEntityIndex(IGroup<TEntity> group, Func<TEntity, IComponent, TKey[]> keys) {
        super(group, keys);
        _index = Collections.createMap(Object.class, Object.class);
        activate();
    }
    @Override
    public void activate() {
        super.activate();
        indexEntities(_group);
    }


    public boolean hasEntity(TKey key) {
        return _index.containsKey(key);
    }

    public TEntity getEntity(TKey key) {
        TEntity entity = tryGetEntity(key);
        if (entity == null) {
            throw new EntityIndexException("Entity for key '" + key + "' doesn't exist!",
                    "You should check if an entity with that key exists before getting it."
            );
        }
        return entity;
    }

    public TEntity tryGetEntity(TKey key) {
        TEntity entity = null;
        _index.get(key);
        return entity;
    }

    @Override
    protected void clear() {
        for (TEntity entity : _index.values()) {
            if(entity.owners().contains(this)) {
                entity.release(this);
            }

        }
        _index.clear();

    }

    @Override
    protected void addEntity(TKey key, TEntity entity) {
        if(_index.containsKey(key)) {
            throw new EntityIndexException(
                    "Entity for key '" + key + "' already exists!",
                    "Only one entity for a primary key is allowed.");
        }

        _index.put(key, entity);
        if(!entity.owners().contains(this)) {
            entity.retain(this);
        }
    }

    @Override
    protected void removeEntity(TKey key, TEntity entity) {
        _index.remove(key);
        if(entity.owners().contains(this)) {
            entity.release(this);
        }
    }


}
